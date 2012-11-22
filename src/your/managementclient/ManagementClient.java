package your.managementclient;

public class ManagementClient {

	private ConsoleCommandListener consoleCommandListener;
	
	public ManagementClient(String analyticsServerBindingName,
			String billingServerBindingName) {
		super();
		consoleCommandListener = new ConsoleCommandListener(analyticsServerBindingName, billingServerBindingName);
	}
	
	public void run() {
		consoleCommandListener.run();
	}
	
	public void stop() {
		
	}
}
