package your.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AnalyticsServer extends Remote {
	void subscribe(String userName, String filter, NotificationCallback callback) throws RemoteException;
	void processEvent() throws RemoteException;
	void unsubscribe() throws RemoteException;
	
}
