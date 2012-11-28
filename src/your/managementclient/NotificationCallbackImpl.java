package your.managementclient;

import java.rmi.RemoteException;

import your.common.helper.Output;
import your.common.rmi.NotificationCallback;

public class NotificationCallbackImpl implements NotificationCallback {

	@Override
	public void send(String msg) throws RemoteException {
		Output.println(msg);
	}

}
