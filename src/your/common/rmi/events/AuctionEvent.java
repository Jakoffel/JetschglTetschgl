package your.common.rmi.events;


public class AuctionEvent extends Event {

	private long auctionID;
	
	public AuctionEvent(String t, long ti, int aid){
		
		super(t);
		auctionID=aid;
	}

	public long getAuctionID() {
		return auctionID;
	}
	
	public long getTimeStamp() {
		return time;
	}
}
