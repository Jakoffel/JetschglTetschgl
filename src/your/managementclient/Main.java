package your.managementclient;

import your.common.helper.Output;

public class Main {

	private static ManagementClient client;
	public static ManagementClient getClient() {
		return client;
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			Output.printError("wrong args for managementClient");
		}

		String analyticsServerBindingName = args[0];
		String billingServerBindingName = args[1];
		
		client = new ManagementClient(analyticsServerBindingName, billingServerBindingName);
		client.run();
		client.stop();
	}

}
