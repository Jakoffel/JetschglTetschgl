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
	
	@Override
	public String toString() {
		String output="";
		if(type.equals("USER_LOGIN")) {
			output="User " +userName+" eingeloggt.";
		}
		if(type.equals("USER_LOGOUT")) {
			output="User " + userName+" ausgeloggt.";
		}
		if(type.equals("USER_DISCONNECTED")) {
			output="User " + userName + " ausgeloggt.";
		}
		return output;
	
	}
}
