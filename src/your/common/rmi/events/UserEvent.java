package your.common.rmi.events;

import java.util.concurrent.atomic.AtomicInteger;

public class UserEvent extends Event {
	
	private String userName;
	private int sessionID;
	
	private static AtomicInteger sessionIdCounter = new AtomicInteger(0);
	
	public UserEvent(String t, String un) {
		super(t);
		userName=un;
		
	}

	public String getUserName() {
		return userName;
	}
	
	public int getSessionID() {
		return sessionID;
	}
	
	private void assignNewSessionID() {
		sessionID = sessionIdCounter.getAndIncrement();
	}
}
