package your.common.rmi.events;


public class AuctionEvent extends Event {

	private long auctionID;
	
	public AuctionEvent(String t, int aid){
		
		super(t);
		auctionID=aid;
	}

	public long getAuctionID() {
		return auctionID;
	}
	
	public long getTimeStamp() {
		return time;
	}
	
	@Override
	public String toString() {
		String output="";
		
		if(type.equals("AUCTION_STARTED")) {
			output="Neue Auktion mit der Auktionsnummer " + auctionID + " erstellt";
		}
		if(type.equals("AUCTION_ENDED")) {
			output="Auktion mit der Auktionsnummer " + auctionID + " beendet.";
		}
		return output;
		
	}
}
