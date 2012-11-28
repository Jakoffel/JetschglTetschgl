package your.common.rmi.events;

public class UserEvent extends Event {
	
	private String userName;
	
	public UserEvent(String t, long ti, String un) {
		super(t, ti);
		userName=un;
	}

	public String getUserName() {
		return userName;
	}
}
