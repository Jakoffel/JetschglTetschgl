package your.managementclient;

import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import your.common.helper.Output;
import your.common.rmi.Bill;
import your.common.rmi.BillingServer;
import your.common.rmi.BillingServerSecure;
import your.common.rmi.PriceSteps;
import your.common.rmi.RmiHostPort;
import your.common.rmi.exceptions.BillingServerException;

public class ConsoleCommandListener {

	private RmiHostPort rhp;

	private BillingServer billingServer;
	private BillingServerSecure billingServerSecure;
	private String userName;

	public ConsoleCommandListener(String analyticsServerBindingName, String billingServerBindingName) {
		super();
		rhp = new RmiHostPort();

		try {
			Registry registry = LocateRegistry.getRegistry(rhp.getHost(), rhp.getPort());
			billingServer = (BillingServer) registry.lookup(billingServerBindingName);
		} catch (RemoteException e) {
			Output.printError("on rmi-zeugs");
		} catch (NotBoundException e) {
			Output.printError("on getting BillingServer");
		}
	}

	public void run() {
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();

		while (!line.equals("!end")) {
			try {
				login(line);
				steps(line);
				addStep(line);
				removeStep(line);
				bill(line);
				logout(line);
			} catch (BillingServerException e) {
				Output.println(e.getMessage());
			} catch (RemoteException e) {
				Output.printError("remote ");
			}
		}
	}

	private void login(String line) throws RemoteException {
		if (!line.contains("!login")) {
			return;
		}

		if (isLoggedIn()) {
			System.out.println("logout first");
			return;
		}

		String args[] = checkArgs(line, 3);

		if (args != null) {
			userName = args[1];
			String pwd = args[2];

			try {
				MessageDigest m = MessageDigest.getInstance("MD5");
				m.update(pwd.getBytes(),0,pwd.length());
				pwd = new BigInteger(1,m.digest()).toString(16);
			} catch (NoSuchAlgorithmException e) {
				Output.printError("create md5-hash for pwd");
			}

			billingServerSecure = billingServer.login(userName, pwd);
			Output.println(userName + "successfully logged in");
		}
	}

	private void steps(String line) throws RemoteException {
		if (!line.equals("!steps")) {
			return;
		}

		if (!isLoggedIn()) {
			Output.println("login first");
			return;
		}

		PriceSteps steps = billingServerSecure.getPriceSteps();
		Output.println(steps.toString());
	}

	private void addStep(String line) throws RemoteException {
		if (!line.contains("!addStep")) {
			return;
		}

		if (!isLoggedIn()) {
			Output.println("login first");
			return;
		}
		
		String args[] = checkArgs(line, 5);
		
		if (args != null) {
			try {
				double start = Double.parseDouble(args[1]);
				double end = Double.parseDouble(args[2]);
				double fixed = Double.parseDouble(args[3]);
				double variable = Double.parseDouble(args[4]);
				
				billingServerSecure.createPriceStep(start, end, fixed, variable);
				Output.println("step successfully added");
			} catch (NumberFormatException e) {
				Output.printError("no strings");
			}
		}
	}

	private void removeStep(String line) throws RemoteException {
		if (!line.contains("!removeStep")) {
			return;
		}
		
		if (!isLoggedIn()) {
			Output.println("login first");
			return;
		}

		String args[] = checkArgs(line, 3);
		
		if (args != null) {
			try {
				double start = Double.parseDouble(args[1]);
				double end = Double.parseDouble(args[2]);
				
				billingServerSecure.deletePriceStep(start, end);
				Output.println("Price step [" + start + " " + end + "] does not exist");
			} catch (NumberFormatException e) {
				Output.printError("no strings");
			}
		}
	}

	private void bill(String line) throws RemoteException {
		if (!line.contains("!bill")) {
			return;
		}
		
		if (!isLoggedIn()) {
			Output.println("login first");
			return;
		}

		String args[] = checkArgs(line, 2);
		
		if (args != null) {
			Bill bill = billingServerSecure.getBill(args[1]);
			Output.println(bill.toString());
		}
	}

	private void logout(String line) {
		if (!line.contains("!removeStep")) {
			return;
		}
		
		if (!isLoggedIn()) {
			Output.println("login first");
			return;
		}

		Output.println(userName + "successfully logged out");
		billingServerSecure = null;
		userName = "";
	}

	private boolean isLoggedIn() {
		return billingServerSecure != null;
	}

	private String[] checkArgs(String line, int length) {
		String[] args = line.split(" ");
		if (args.length != length) {
			System.out.println("invalid args");
			return null;
		}

		return args;
	}
}
