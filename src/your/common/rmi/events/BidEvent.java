package your.common.rmi.events;

public class BidEvent extends Event {

	private String userName;
	private double price;
	private long auctionID;
	
	public BidEvent(String t, String un, double pr, int aid) {
		super(t);
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
	
	@Override
	public String toString() {
		String output="";
		if(type.equals("BID_PLACED")) {
			output="Neues Gebot von " +userName + "für Auktion mit der Auktionsnummer " + auctionID + " abgegeben. Gebotshöhe: " + price;
		}
		if(type.equals("BID_OVERBID")) {
			output="Neues Höchstgebot durch " + userName + " für Auktion mit der Auktionsnummer " + auctionID +". Gebotshöhe: " + price;
		}
		if(type.equals("BID_WON")) {
			output="Bieter " + userName + " gewinnt Auktion mit der Auktionsnummer " + auctionID + " Preis: " + price;
		}
		return output;
	}
	
}
