package your.server;


import your.common.helper.MyThreadPool;
import your.common.helper.Output;
import your.common.rmi.events.Event;
import your.server.commands.ClientCommandListener;
import your.server.commands.ConsoleCommandsListener;
import your.server.management.AuctionManagement;
import your.server.management.UserManagement;

public class Server {

	private AnalyticServerHeinz analyticServer;
	
	private UserManagement userManagement;
	private AuctionManagement auctionManagement;
	private ClientCommandListener clientCommandListener;
	private ConsoleCommandsListener consoleCommandListener;
	
	public Server(int tcpPort, String analyticsBindingName, String billingBindingName) {
		userManagement = new UserManagement();
		auctionManagement = new AuctionManagement(billingBindingName);
		clientCommandListener = new ClientCommandListener(tcpPort);
		consoleCommandListener = new ConsoleCommandsListener();
		analyticServer = new AnalyticServerHeinz(analyticsBindingName);
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

	public void processEvent(Event event) {
		analyticServer.processEvent(event);
	}
}
