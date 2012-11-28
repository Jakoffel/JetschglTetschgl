package your.analyticsserver;

import java.util.concurrent.atomic.AtomicInteger;

import your.common.rmi.NotificationCallback;

public class ManagementUser {
	
	private static AtomicInteger idCounter = new AtomicInteger(0);
	private int userID;
	private NotificationCallback callback;
	private String filter;
	
	public ManagementUser(String filter, NotificationCallback callback) {

		this.callback = callback;
		this.filter = filter;
		assignNewUserID();
	}

	public int getUserId() {
		return userID;
	}

	public NotificationCallback getCallback() {
		return callback;
	}

	public String getFilter() {
		return filter;
	}
	
	private void assignNewUserID() {
		userID = idCounter.incrementAndGet();
	}
	
	
	
	
	
}
