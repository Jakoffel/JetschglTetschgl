package your.common.rmi.events;

public class StatisticsEvent extends Event {
	
	private double value;

	public StatisticsEvent(String t, long ti, double v) {
		super(t, ti);
		value=v;
		
	}

	public double getValue() {
		return value;
	}
}
