package your.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import your.common.helper.Output;
import your.common.rmi.AnalyticsServer;
import your.common.rmi.RmiHostPort;
import your.common.rmi.events.Event;

public class AnalyticServerHeinz {

	private AnalyticsServer analyticServer;
	
	public AnalyticServerHeinz(String analyticServerBindingName) {
		RmiHostPort rhp = new RmiHostPort();
		
		try {
			Registry registry = LocateRegistry.getRegistry(rhp.getHost(), rhp.getPort());
			analyticServer = (AnalyticsServer) registry.lookup(analyticServerBindingName);
		} catch (RemoteException e) {
			Output.printError("reference (a stub) to the remote object registry could not be created");
		} catch (NotBoundException e) {
			Output.printError(analyticServerBindingName + " is not currently bound");
		}
	}
	
	public void processEvent(Event event) {
		try {
			analyticServer.processEvent(event);
		} catch (RemoteException e) {
			Output.printError("on sending event to analyticserver");
		}
	}
}
