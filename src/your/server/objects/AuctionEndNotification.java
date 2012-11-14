package your.server.objects;

import java.rmi.ServerException;

public class AuctionEndNotification  extends Notification {
	
	private String winnerName;
	
	public AuctionEndNotification(String ownerName, int auctionId, String winnerName) {
		super(ownerName, auctionId);
		this.winnerName = winnerName;
	}

	@Override
	public String printMessage() {
		try {
			return "!auction-ended " + winnerName + " " + getAuctionBid() + " " + getAuctionDescription();
		} catch (ServerException e) {
			return e.getMessage();
		}
	}

}
