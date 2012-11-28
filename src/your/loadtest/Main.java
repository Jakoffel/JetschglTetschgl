package your.loadtest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Properties;

import your.common.helper.Output;

public class Main {
	
	private static LoadTester loadTest;
	public static LoadTester getLoadTest() {
		return loadTest;
	}
	
	private static double bid;
	public static synchronized BigDecimal getBid() {
		bid += 0.01;
		return new BigDecimal(bid);
	}

	public static void main(String[] args) {
		bid = 0d;
		
		//ARGS - serverhost,serverport,analyticserver,billingserver
		String serverHost = args[0];
		int serverPort = Integer.parseInt(args[1]);
		String analyticsBindingName = args[2];
		String billingServerBindingName = args[3];

		//Loadtest-Properties
		int clients = -1;
		int auctionsPerMin = -1;
		int auctionDuration = -1;
		int updateIntervalSec = -1;
		int bidsPerMin = -1;

		Properties proLoad = new Properties();
		try{
			java.io.InputStream in = ClassLoader.getSystemResourceAsStream("loadtest.properties");
			proLoad.load(in);
			Enumeration<Object> em = proLoad.keys();

			String key = "";
			key = (String) em.nextElement();
			auctionsPerMin = Integer.parseInt(proLoad.getProperty(key));

			key = (String) em.nextElement();
			clients = Integer.parseInt(proLoad.getProperty(key));
			
			key = (String) em.nextElement();
			auctionDuration = Integer.parseInt(proLoad.getProperty(key));

			key = (String) em.nextElement();
			updateIntervalSec = Integer.parseInt(proLoad.getProperty(key));

			key = (String) em.nextElement();
			bidsPerMin = Integer.parseInt(proLoad.getProperty(key));
		
		} catch(IOException e){
			Output.println("Error @ reading registry properties");
		}
		
		loadTest = new LoadTester(serverHost, serverPort, analyticsBindingName, billingServerBindingName, clients, auctionsPerMin, auctionDuration, updateIntervalSec, bidsPerMin);
		loadTest.run();
	}
}
