package your.server.objects;

import java.math.BigDecimal;
import java.rmi.ServerException;

import your.server.Main;

public abstract class Notification {
	protected String ownerName;
	protected int auctionId;
	protected String message;
	
	public Notification(String ownerName, int auctionId) {
		this.ownerName = ownerName;
		this.auctionId = auctionId;
	}
	
	public boolean belongsTo(User user) {
		return user.getName().equals(ownerName);
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	protected String getAuctionDescription() throws ServerException {
		return Main.getAuctionManagement().getAuction(auctionId).getDescription();
	}
	
	protected String getAuctionBidderName() throws ServerException {
		return Main.getAuctionManagement().getAuction(auctionId).getBidderName();
	}
	
	protected BigDecimal getAuctionBid() throws ServerException {
		return Main.getAuctionManagement().getAuction(auctionId).getHighestBid();
	}
	
	public abstract String printMessage();
}
