package your.common.rmi.events;

public class BidEvent extends Event {

	private String userName;
	private double price;
	private long auctionID;
	
	public BidEvent(String t, long ti, String un, double pr, int aid) {
		super(t, ti);
		userName=un;
		auctionID=aid;
		price=pr;
	}
	
	public String getUserName() {
		return userName;
	}

	public double getPrice() {
		return price;
	}


	public long getAuctionID() {
		return auctionID;
	}
	
}
