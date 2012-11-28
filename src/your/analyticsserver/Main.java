package your.analyticsserver;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import your.common.helper.Output;
import your.common.rmi.AnalyticsServer;

public class Main {

	private static String analyticsServerBindingName;
	
	private static AnalyticsServerImpl analyticsServer;
	
	public static AnalyticsServerImpl getAnalyticsServer() {
		return analyticsServer;
	}
	
	public static void main(String[] args) {
		if (args.length == 1) {
			analyticsServerBindingName = args[0];
			
			analyticsServer = new AnalyticsServerImpl();
			if (exportRemoteObject()) {
				analyticsServer.run();
				unexportRemoteObject();
			}
			
		} else {
			Output.println("argument analyticsserver-bindingname is missing");
		}

	}
	
	private static boolean exportRemoteObject() {
		Registry registry = null;
		try {
            registry = LocateRegistry.createRegistry(analyticsServer.getRmiPort());            
        } catch (ExportException e) {
        	try {
				registry = LocateRegistry.getRegistry(analyticsServer.getRmiPort());
			} catch (RemoteException e1) {
				Output.printError("on get Registry"); 
				return false;
			}  
        } catch (RemoteException e) {
        	Output.printError("on create Registry"); 
			return false;
		}
		
        try {
        	AnalyticsServer stub = (AnalyticsServer) UnicastRemoteObject.exportObject(analyticsServer, analyticsServer.getRmiPort());
	        registry.rebind(analyticsServerBindingName, stub);
		} catch (RemoteException e) {
			Output.printError("on exporting Remote Object"); 
			return false;
		}
        
		return true;
	}
	
	private static void unexportRemoteObject() {
		try {
			UnicastRemoteObject.unexportObject(analyticsServer, true);
			
			Output.println("AnalyticsServer unexported");
		} catch (NoSuchObjectException e) {
			Output.printError("on unexporting Remote Object");
		}
	}
}
