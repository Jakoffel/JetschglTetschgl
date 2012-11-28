package your.common.rmi.events;

public class StatisticsEvent extends Event {
	
	private double value;

	public StatisticsEvent(String t, double v) {
		super(t);
		value=v;
		
	}

	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String output="";
		if(type.equals("USER_SESSION_MAX")) {
			output="Längste Online-Zeit eines Users: " + value;
		}
		if(type.equals("USER_SESSION_MIN")) {
			output="Kürzeste Online-Zeit eines Users: " + value;
		}
		if(type.equals("USER_SESSION_AVG")) {
			output="Durchschnittle Online-Zeit der User: " + value;
		}
		if(type.equals("BID_PRICE_MAX")) {
			output="Höchestes Gebot bisher: " + value;
		}
		if(type.equals("BID_COUNT_PER_MINUTE")) {
			output="Durchschnittlichen Gebote pro Minute";
		}
		if(type.equals("AUCTION_TIME_AVG")) {
			output="Durchschnittliche Dauer einer Auktion: " + value;
		}
		if(type.equals("AUCTION_SUCCESS_RATIO")) {
			output="Anteil der Auktionen für die mindestens ein Gebot abgegeben wird: " + value;
		}
		return output;
	}
}
