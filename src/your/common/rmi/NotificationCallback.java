package your.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationCallback extends Remote {
	public void send(String msg) throws RemoteException;
}
