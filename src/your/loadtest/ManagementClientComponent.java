package your.loadtest;

import your.managementclient.ManagementClient;

public class ManagementClientComponent implements Runnable {
	private ManagementClient mgmtClient;
	
	public ManagementClientComponent(String analyticsServerBindingName,
			String billingServerBindingName) {
		super();
		mgmtClient = new ManagementClient(analyticsServerBindingName, billingServerBindingName);
	}

	@Override
	public void run() {
		mgmtClient.toString();
		
	}

	public void stop() {
		
		
	}
	
	
}
