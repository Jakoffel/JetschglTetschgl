package your.client;

public class Main {

	private static Client client;
	
	public static Client getClient() {
		return client;
	}
	
	public static void main(String args[]) {
		if (!checkArgs(args)) {
			System.out.println(usage());
			return;
		}
		
		client = getFrom(args);
		client.start();
	}

	private static boolean checkArgs(String[] args) {
		boolean ok = true;
		
		if (args.length != 3) {
			ok = false;
		}
		
		if (args[0].length() <= 0) {
			ok = false;
		}
		
		try {
			Integer.parseInt(args[1]);
			Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			ok = false;
		}
		
		return ok;
	}

	private static Client getFrom(String[] args) {
		String serverHost = args[0];
		int serverTcpPort = Integer.parseInt(args[1]);
		int clientUdpPort = Integer.parseInt(args[2]);
		
		return new Client(serverHost, serverTcpPort, clientUdpPort);
	}

	private static String usage() {
		return "The client application should expect the following arguments: \n" +
				"  host: host name or IP of the auction server\n" + 
				"  tcpPort: TCP connection port on which the auction server is listening for incoming connections\n" + 
				"  udpPort: this port will be used for instantiating a java.net.DatagramSocket (handling UDP notifications from the auction server). ";
	}
}
