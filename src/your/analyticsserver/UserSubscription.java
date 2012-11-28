package your.analyticsserver;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

import your.common.helper.Output;
import your.common.rmi.NotificationCallback;

public class UserSubscription {
	
	private static AtomicInteger idCounter = new AtomicInteger(0);
	private int subscriptionID;
	private NotificationCallback callback;
	private String filter;
	
	public UserSubscription(String filter, NotificationCallback callback) {

		this.callback = callback;
		this.filter = filter;
		assignNewSubscriptionID();
	}

	public int getSubscriptionID() {
		return subscriptionID;
	}

	public NotificationCallback getCallback() {
		return callback;
	}

	public String getFilter() {
		return filter;
	}
	
	private void assignNewSubscriptionID() {
		subscriptionID = idCounter.incrementAndGet();
	}
	
	public void sendNotification(String msg) {
		try {
			callback.send(msg);
		} catch (RemoteException e) {
			Output.printError("on sending notification");
		}
	}
	
	
	
	
}
