package your.server.management;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import your.common.rmi.events.AuctionEvent;
import your.common.rmi.events.BidEvent;
import your.server.Main;
import your.server.objects.Auction;
import your.server.objects.AuctionEndNotification;

public class AuctionManagement {

	private List<Auction> auctions;
	private Timer timer;
	private BillServerHeinz billServerHeinz;
	
	public AuctionManagement(String billingBindingName) {
		billServerHeinz = new BillServerHeinz(billingBindingName);
		auctions = Collections.synchronizedList(new ArrayList<Auction>());
		timer = new Timer();
	}
	
	public void stopTimerTasks() {
		timer.cancel();
	}
	
	public synchronized Auction createAuction(String ownerName, String description, int endInSeconds) throws ServerException {
		checkUserLoggedIn(ownerName);
		
		Auction auction = new Auction(description, ownerName, endInSeconds);
		auctions.add(auction);
		timer.schedule(createAuctionEndTask(auction), auction.getEndDate());
		
		Main.processEvent(new AuctionEvent("AUCTION_STARTED", 1, auction.getId()));
		return auction;
	}
	
	public synchronized String printOpenAuctions() {
		String auctionList = "";
		
		for (Auction auction : auctions) {
			if (auction.hasEnded()) {
				continue;
			}
			
			auctionList += auction + "\n";
		}
		
		return auctionList;
	}
	
	public synchronized Auction placeBid(String bidderName, int auctionId, BigDecimal amount) throws ServerException {
		checkUserLoggedIn(bidderName);
		
		Auction auction = getAuction(auctionId);
		auction.placeBid(bidderName, amount);
		
		return auction;
	}
		
	public synchronized Auction getAuction(int id) throws ServerException {
		for (Auction auction : auctions) {
			if (auction.getId() == id) { 
				return auction;
			}
		}
		
		throw new ServerException("Auction with id: " + id + " not found!");
	}
	
	private void checkUserLoggedIn(String userName) throws ServerException {
		if (!Main.getUserManagement().isUserLoggedIn(userName)) {
			throw new ServerException("User " + userName + " is not logged in!");
		}
	}
	
	private TimerTask createAuctionEndTask(final Auction auction) {
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				AuctionEndNotification owner = new AuctionEndNotification(auction.getOwnerName(), auction.getId(), auction.getBidderName());
				AuctionEndNotification winner = new AuctionEndNotification(auction.getBidderName(), auction.getId(), "You");
				
				Main.getUserManagement().sendNotificationTo(owner);
				Main.getUserManagement().sendNotificationTo(winner);
				
				billServerHeinz.billAuction(auction.getOwnerName(), auction.getId(), auction.getHighestBid().doubleValue());
				Main.processEvent(new AuctionEvent("AUCTION_ENDED", 1, auction.getId()));
				Main.processEvent(new BidEvent("BID_WON", 1, auction.getBidderName(), auction.getHighestBid().doubleValue(), auction.getId()));
			}
		};
		
		return task;
	}
}
