package your.server.management;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import your.common.helper.Output;
import your.common.rmi.BillingServer;
import your.common.rmi.BillingServerSecure;
import your.common.rmi.RmiHostPort;

public class BillServerHeinz {
	BillingServerSecure serverSecure;
	
	public BillServerHeinz(String billingBindingName) {
		RmiHostPort rhp = new RmiHostPort();
		
		try {
			Registry registry = LocateRegistry.getRegistry(rhp.getHost(), rhp.getPort());
			BillingServer server = (BillingServer) registry.lookup(billingBindingName);
			serverSecure = server.login("auctionServer", "fa062259ac376b119949f55de4f4c420");
		} catch (RemoteException e) {
			Output.printError("reference (a stub) to the remote object registry could not be created");
		} catch (NotBoundException e) {
			Output.printError(billingBindingName + " is not currently bound");
		}
	}
	
	public void billAuction(String user, long auctionId, double price) {
		if (serverSecure != null) {
			try {
				serverSecure.billAuction(user, auctionId, price);
			} catch (RemoteException e) {
				Output.printError("on sending auction-data to billingserver");
			}
		} else {
			Output.printError("serverSecure-object not generated");
		}
	}
}
