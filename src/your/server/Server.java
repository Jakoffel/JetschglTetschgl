package your.server;


import your.common.helper.MyThreadPool;
import your.common.helper.Output;
import your.server.commands.ClientCommandListener;
import your.server.commands.ConsoleCommandsListener;
import your.server.management.AuctionManagement;
import your.server.management.UserManagement;

public class Server {

	private UserManagement userManagement;
	private AuctionManagement auctionManagement;
	private ClientCommandListener clientCommandListener;
	private ConsoleCommandsListener consoleCommandListener;
	
	private String analyticsBindingName;
	private String billingBindingName;
	
	public Server(int tcpPort, String analyticsBindingName, String billingBindingName) {
		userManagement = new UserManagement();
		auctionManagement = new AuctionManagement();
		clientCommandListener = new ClientCommandListener(tcpPort);
		consoleCommandListener = new ConsoleCommandsListener();
		
		this.analyticsBindingName = analyticsBindingName;
		this.billingBindingName = billingBindingName;
	}
	
	public UserManagement getUserManagement() {
		return userManagement;
	}
	
	public AuctionManagement getAuctionManagement() {
		return auctionManagement;
	}

	public void start() {
		Output.h1("Server-Start");
		MyThreadPool.execute(clientCommandListener);
		consoleCommandListener.run();
	}
	
	public void stop() {
		Output.h1("Server-End");
		clientCommandListener.stopListening();
		auctionManagement.stopTimerTasks();
		MyThreadPool.shutdown();
	}

	public String getAnalyticsBindingName() {
		return analyticsBindingName;
	}

	public String getBillingBindingName() {
		return billingBindingName;
	}
}
