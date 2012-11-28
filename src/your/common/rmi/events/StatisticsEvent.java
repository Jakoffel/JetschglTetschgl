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
}
