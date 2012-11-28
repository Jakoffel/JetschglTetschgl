package your.loadtest;

import java.util.Scanner;

import your.common.helper.MyThreadPool;

public class LoadTester {
	
	private ClientComponent client;
	private ManagementClientComponent managementClient;
	

	public LoadTester(String serverHost, int serverPort,
			String analyticsBindingName, String billingServerBindingName,
			int clients, int auctionsPerMin, int auctionDuration,
			int updateIntervalSec, int bidsPerMin) {
		super();
		client = new ClientComponent(clients, auctionsPerMin, auctionDuration, updateIntervalSec, bidsPerMin, serverHost, serverPort);
		managementClient = new ManagementClientComponent(analyticsBindingName, billingServerBindingName);		
	}

	public void run() {
		MyThreadPool.execute(client);
		MyThreadPool.execute(managementClient);
		
		Scanner in = new Scanner(System.in);
		while (!in.nextLine().equals("!end")) {}
		client.stop();
		managementClient.stop();
	}
	
}
