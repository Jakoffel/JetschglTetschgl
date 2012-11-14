package your.server.objects;

import java.rmi.ServerException;

public class NewBidNotification extends Notification {

	public NewBidNotification(String ownerName, int auctionId) {
		super(ownerName, auctionId);
	}

	@Override
	public String printMessage() {
		try {
			return "!new-bid " + getAuctionDescription();
		} catch (ServerException e) {
			return e.getMessage();
		}
	}

}
