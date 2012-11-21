package your.client;

import your.common.helper.MyThreadPool;
import your.common.helper.Output;

public class Client {
	
	private int clientUdpPort;
	private String currentUserName = "";
	
	//private ServerNotificationListener serverNotificationListener;
	private ConsoleCommandListener consoleCommandListener;
	
	public Client(String serverHost, int serverTcpPort, int clientUdpPort) {
		this.clientUdpPort = clientUdpPort;
		//serverNotificationListener = new ServerNotificationListener();
		consoleCommandListener = new ConsoleCommandListener(serverHost, serverTcpPort);
	}

	public void start() {
		Output.h1("Client-Start");
		//MyThreadPool.execute(serverNotificationListener);
		consoleCommandListener.start();
	}
	
	public void shutdown() {
		Output.h1("Client-End");
		//serverNotificationListener.stop();
		consoleCommandListener.stop();
		MyThreadPool.shutdown();
	}
	
	public int getClientUdpPort() {
		return clientUdpPort;
	}
	
	public String getCurrentUserName() {
		return currentUserName;
	}

	public void login(String name) {
		currentUserName = name;
	}
	
	public void logout() {
		currentUserName = "";
	}
	
	public boolean isUserLoggedIn() {
		return currentUserName.length() > 0;
	}

	
}
