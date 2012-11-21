package your.billingserver;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import your.billingserver.objects.User;
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
			List<User> users = getUsersFromPropertyFile();
			
			billingServer = new BillingServerImpl(users);
			exportRemoteObject();
			billingServer.run();
			unexportRemoteObject();
			
		} else {
			Output.println("argument billingserver-bindingname is missing");
		}
	}

	private static List<User> getUsersFromPropertyFile() {
		Properties pro = new Properties();
		try{
			java.io.InputStream in = ClassLoader.getSystemResourceAsStream("user.properties");
			pro.load(in);
			Enumeration<Object> em = pro.keys();
			List<User> users = Collections.synchronizedList(new ArrayList<User>());
			
			while (em.hasMoreElements()) {
				String user = (String) em.nextElement();
				String pwd = pro.getProperty(user);
				users.add(new User(user, pwd));
			}
			
			return users;
		} catch(IOException e){
			Output.println("Error @ reading users");
		}
		
		return null;
	}
	
	private static void exportRemoteObject() {
		try {
            Registry registry = LocateRegistry.createRegistry(billingServer.getRmiPort());            
            BillingServer stub = (BillingServer) UnicastRemoteObject.exportObject(billingServer, billingServer.getRmiPort());
            registry.rebind(billingServerBindingName, stub);
            
            Output.println("BillingServer exported");
        } catch (Exception e) {
            Output.println("error on exporting Remote Object");    
        }
	}
	
	private static void unexportRemoteObject() {
		try {
			UnicastRemoteObject.unexportObject(billingServer, true);
			
			Output.println("BillingServer unexported");
		} catch (NoSuchObjectException e) {
			Output.println("error on unexporting Remote Object");
		}
	}
}
