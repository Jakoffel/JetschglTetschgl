package your.server.management;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import your.common.rmi.events.UserEvent;
import your.server.Main;
import your.server.objects.Notification;
import your.server.objects.User;

public class UserManagement {
	private List<User> users;
	private List<Notification> unsendNotifications;
	private NotificationSender notificationSender;
	
	public UserManagement() {
		users = Collections.synchronizedList(new ArrayList<User>());
		unsendNotifications = Collections.synchronizedList(new ArrayList<Notification>());
		notificationSender = new NotificationSender();
	}
	
	public synchronized boolean login(String name, InetAddress clientHost, int udpPort) {
		User user = getUser(name);
		
		if (user.isLoggedIn()) {
			return false;
		} else {
			user.login();
			user.setClientHost(clientHost);
			user.setUdpPort(udpPort);
			checkUnsentNotifications(user);
			Main.processEvent(new UserEvent("USER_LOGIN", 1, name));
			return true;
		}
	}
	
	private void checkUnsentNotifications(User user) {
		ArrayList<Notification> toSend = new ArrayList<Notification>();
		
		for (Notification notification : unsendNotifications) {
			if (notification.belongsTo(user)) {
				toSend.add(notification);
			}
		}
		
		unsendNotifications.removeAll(toSend);
		notificationSender.sendNotifications(toSend);
	}

	public synchronized boolean logout(String name) {
		User user = getUser(name);
		
		if (user.isLoggedIn()) {
			user.logout();
			Main.processEvent(new UserEvent("USER_LOGOUT", 1, name));
			return true;
		} else {
			return false;			
		}
	}
	
	public synchronized void sendNotificationTo(Notification notification) {
		User owner = getUser(notification.getOwnerName());
		
		if (owner.isLoggedIn()) {
			notificationSender.sendNotification(notification);
		} else {
			unsendNotifications.add(notification);
		}
	}
	
	public synchronized User getUser(String name) {
		for (User user : users) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		
		//user with name not found, create new one
		User user = new User(name);		
		users.add(user);
		return user;
	}
	
	public synchronized boolean isUserLoggedIn(String name) {
		for (User user : users) {
			if (user.getName().equals(name)) {
				return user.isLoggedIn();
			}
		}
		//user with name not found, so the user can not be logged in
		return false;
	}
}
