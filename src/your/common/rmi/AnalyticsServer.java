package your.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import your.common.rmi.events.Event;

public interface AnalyticsServer extends Remote {
	int subscribe(String filter, NotificationCallback callback) throws RemoteException;
	void processEvent(Event event) throws RemoteException;
	void unsubscribe(int userId) throws RemoteException;	
}
