package your.billingserver;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import your.billingserver.objects.ManagementUser;
import your.common.helper.Output;
import your.common.rmi.BillingServer;
import your.common.rmi.BillingServerSecure;
import your.common.rmi.RmiHostPort;
import your.common.rmi.exceptions.BillingServerException;

public class BillingServerImpl implements BillingServer {
	
	private List<ManagementUser> managementUsers;
	private RmiHostPort rhp;
	private ConsoleCommandsListener consoleCommandsListener;
	private BillingServerSecure billingServerSecure;
	
	public BillingServerImpl(List<ManagementUser> users) {
		this.managementUsers = users;
		rhp = new RmiHostPort();
		consoleCommandsListener = new ConsoleCommandsListener();
		billingServerSecure = new BillingServerSecureImpl();
	}
	
	@Override
	public synchronized BillingServerSecure login(String userName, String password) throws RemoteException {
		ManagementUser login = new ManagementUser(userName, password);
		
		for (ManagementUser user : managementUsers) {
			if (user.equals(login)) {
				return billingServerSecure;
			}
		}
		
		throw new BillingServerException("User with pwd not found");
	}

	public void run() {
		exportBillingServerSecure();
		consoleCommandsListener.run();
		stop();
	}

	private void stop() {
		unexportBillingServerSecure();
	}

	private void exportBillingServerSecure() {
		try {
			UnicastRemoteObject.exportObject(billingServerSecure, rhp.getPort());
		} catch (RemoteException e) {
			Output.printError("on exporting BillingServerSecure");
		}
	}
	
	private void unexportBillingServerSecure() {
		try {				
			UnicastRemoteObject.unexportObject(billingServerSecure, true);
		} catch (NoSuchObjectException e) {
			Output.printError("on unexporting BillingServerSecure");
		}
	}

	public List<ManagementUser> getUsers() {
		return managementUsers;
	}
	
	public int getRmiPort() {
		return rhp.getPort();
	}
}
