package your.billingserver;

import java.rmi.RemoteException;
import java.util.List;

import your.billingserver.objects.User;
import your.common.rmi.BillingServer;
import your.common.rmi.BillingServerSecure;
import your.common.rmi.RmiHostPort;

public class BillingServerImpl implements BillingServer {
	
	private List<User> users;
	private RmiHostPort rhp;
	private ConsoleCommandsListener consoleCommandsListener;
	
	public BillingServerImpl(List<User> users) {
		this.users = users;
		rhp = new RmiHostPort();
		consoleCommandsListener = new ConsoleCommandsListener();
	}
	
	@Override
	public BillingServerSecure login(String username, String password) throws RemoteException {
		return null;
	}

	public void run() {
		consoleCommandsListener.run();
		stop();
	}

	private void stop() {
		
	}

	
	public List<User> getUsers() {
		return users;
	}
	
	public int getRmiPort() {
		return rhp.getPort();
	}
}
