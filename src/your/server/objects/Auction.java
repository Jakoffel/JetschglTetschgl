package your.server.objects;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import your.server.Main;

public class Auction {

	private static AtomicInteger idCounter = new AtomicInteger(0);
	
	private int id;
	private String description;
	private String ownerName;
	private Date end;
	private Bid highestBid;
	
	private String noBidder = "none";
	
	public Auction(String description, String ownerName, int endInSeconds) {
		this.description = description;
		this.ownerName = ownerName;
		this.highestBid = new Bid(new BigDecimal("0.00"), noBidder);
		
		assignNewUniqueId();
		calculateEndDate(endInSeconds);
	}
	
	private void calculateEndDate(int endInSeconds) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, endInSeconds);
		end = c.getTime();		
	}

	private void assignNewUniqueId() {
		id = idCounter.incrementAndGet();
	}
	
	public synchronized boolean hasEnded() {
		return end.compareTo(new Date()) < 0;
	}
	
	public synchronized void placeBid(String bidderName, BigDecimal bid) throws ServerException {
		checkBid(bidderName, bid);				
		sendOverBidNotification(bidderName);
		highestBid = new Bid(bid, bidderName);
	}

	private void checkBid(String bidderName, BigDecimal bid) throws ServerException {
		if (bidderName.equals(ownerName)) {
			throw new ServerException("An Owner can not bid on own auction!");
		}
		
		if (hasEnded()) {
			throw new ServerException("Auction with id: " + id + " is over!");
		}

		if (bid.compareTo(highestBid.bid) <= 0) {
			throw new ServerException(printUnsuccessfullyBid(bid));
		}		
	}
	
	private void sendOverBidNotification(String bidderName) {
		if (getBidderName().equals(noBidder)) {
			return;
		}
		if (getBidderName().equals(bidderName)) {
			return;
		}
		
		NewBidNotification oNot = new NewBidNotification(getBidderName(), id);
		Main.getUserManagement().sendNotificationTo(oNot);
	}

	public synchronized boolean isAvailable() {
		return end.compareTo(new Date()) < 0;
	}
	
	@Override
	public synchronized String toString() {
		return id + ". " + description + " " + ownerName + " " + end + " " + highestBid;
	}
	
	public synchronized String printCreate() {
		return "An auction '" + description + "' with id " + id + " has been created and " +
				"will end on " + end + ".";
	}
	
	public synchronized String printSuccessfullyBid() {
		return "You successfully bid with " + getHighestBid() + " on '" + description + "'.";
	}
	
	public synchronized String printUnsuccessfullyBid(BigDecimal bid) {
		return "You unsuccessfully bid with " + bid + " on '" + description + "'. Current highest bid is " + getHighestBid() + ".";
	}
	
	public synchronized String getDescription() {
		return description;
	}
	
	public synchronized String getOwnerName() {
		return ownerName;
	}
	
	public synchronized Date getEndDate() {
		return end;
	}
	
	public synchronized String getBidderName() {
		return highestBid.bidderName;
	}
	
	public synchronized BigDecimal getHighestBid() {
		return highestBid.bid.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public synchronized boolean hasBid() {
		return !highestBid.bidderName.equals("none");
	}
	
	public synchronized int getId() {
		return id;
	}
	
	private class Bid {
		private BigDecimal bid;
		private String bidderName;
		
		Bid(BigDecimal bid, String bidderName) {
			this.bid = bid;
			this.bidderName = bidderName;
		}
		
		@Override
		public String toString() {
			return getHighestBid() + " " + bidderName;
		}
	}
}
