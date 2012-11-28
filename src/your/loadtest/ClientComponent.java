package your.loadtest;

import java.util.ArrayList;

public class ClientComponent implements Runnable {
 
	private ArrayList<ClientTester> clientTesters;
	
	public ClientComponent(int clients, int auctionsPerMin,
			int auctionDuration, int updateIntervalSec, int bidsPerMin,
			String serverHost, int serverPort) {
		super();
		clientTesters = new ArrayList<ClientTester>();
		
		for (int i = 0; i < clients; i++) {
			ClientTester tester = new ClientTester(auctionsPerMin, auctionDuration, updateIntervalSec, bidsPerMin, serverHost, serverPort);
			clientTesters.add(tester);
		}
	}

	@Override
	public void run() {
		for (ClientTester tester : clientTesters) {
			tester.run();
		}
	}

	public void stop() {
		for (ClientTester tester : clientTesters) {
			tester.stop();
		}
	}
}
