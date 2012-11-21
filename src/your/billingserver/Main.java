package your.billingserver;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import your.billingserver.objects.ManagementUser;
import your.common.helper.Output;
import your.common.rmi.BillingServer;

public class Main {
	
	private static String billingServerBindingName;
	
	private static BillingServerImpl billingServer;
	public static BillingServerImpl getBillingServer() {
		return billingServer;
	}
	
	public static void main(String[] args) {
		if (args.length == 1) {
			billingServerBindingName = args[0];
			List<ManagementUser> users = getManagementUsersFromPropertyFile();
			
			billingServer = new BillingServerImpl(users);
			if (exportRemoteObject()) {
				billingServer.run();
				unexportRemoteObject();
			}
			
		} else {
			Output.println("argument billingserver-bindingname is missing");
		}
	}

	private static List<ManagementUser> getManagementUsersFromPropertyFile() {
		Properties pro = new Properties();
		try{
			java.io.InputStream in = ClassLoader.getSystemResourceAsStream("user.properties");
			pro.load(in);
			Enumeration<Object> em = pro.keys();
			List<ManagementUser> users = Collections.synchronizedList(new ArrayList<ManagementUser>());
			
			while (em.hasMoreElements()) {
				String user = (String) em.nextElement();
				String pwd = pro.getProperty(user);
				users.add(new ManagementUser(user, pwd));
			}
			
			return users;
		} catch(IOException e){
			Output.println("Error @ reading users");
		}
		
		return null;
	}
	
	private static boolean exportRemoteObject() {
		Registry registry = null;
		try {
            registry = LocateRegistry.createRegistry(billingServer.getRmiPort());            
        } catch (ExportException e) {
        	try {
				registry = LocateRegistry.getRegistry(billingServer.getRmiPort());
			} catch (RemoteException e1) {
				Output.printError("on get Registry"); 
				return false;
			}  
        } catch (RemoteException e) {
        	Output.printError("on create Registry"); 
			return false;
		}
		
        try {
        	BillingServer stub = (BillingServer) UnicastRemoteObject.exportObject(billingServer, billingServer.getRmiPort());
	        registry.rebind(billingServerBindingName, stub);
		} catch (RemoteException e) {
			Output.printError("on exporting Remote Object"); 
			return false;
		}
        
		return true;
	}
	
	private static void unexportRemoteObject() {
		try {
			UnicastRemoteObject.unexportObject(billingServer, true);
			
			Output.println("BillingServer unexported");
		} catch (NoSuchObjectException e) {
			Output.printError("on unexporting Remote Object");
		}
	}
}
