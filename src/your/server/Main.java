package your.server;

import your.common.rmi.events.Event;
import your.server.management.AuctionManagement;
import your.server.management.UserManagement;

public class Main {

	private static Server server;
	
	public static Server getServer() {
		return server;
	}
	
	public static void processEvent(Event event) {
		server.processEvent(event);
	}
	
	public static UserManagement getUserManagement() {
		return server.getUserManagement();
	}
	
	public static AuctionManagement getAuctionManagement() {
		return server.getAuctionManagement();
	}
	
	public static void main(String args[]) {
		if (!checkArgs(args)) {
			System.out.println(usage());
			return;
		}
		
		server = getFrom(args);
		server.start();
	}

	private static boolean checkArgs(String[] args) {
		boolean ok = true;
		
		if (args.length != 3) {
			ok = false;
		}
		
		try {
			Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			ok = false;
		}
		
		if (args[1].length() == 0) {
			ok = false;
		}
		
		if (args[2].length() == 0) {
			ok = false;
		}
		
		return ok;
	}

	private static Server getFrom(String[] args) {
		return new Server(Integer.parseInt(args[0]), args[1], args[2]);
	}

	private static String usage() {
		return "The auction server application should expect the following arguments: \n" +
				"tcpPort: TCP connection port on which the auction server will receive incoming messages (commands from) clients. ";
	}
}
