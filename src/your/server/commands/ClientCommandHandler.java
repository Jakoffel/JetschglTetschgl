package your.server.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.ServerException;
import java.util.concurrent.atomic.AtomicBoolean;

import your.common.commands.BidCommand;
import your.common.commands.Command;
import your.common.commands.CreateCommand;
import your.common.commands.EndCommand;
import your.common.commands.ListCommand;
import your.common.commands.LoginCommand;
import your.common.commands.LogoutCommand;
import your.common.helper.Output;
import your.server.Main;
import your.server.objects.Auction;

public class ClientCommandHandler implements Runnable {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private AtomicBoolean listenToClient = new AtomicBoolean(true); 

	public ClientCommandHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		createStreams();
		listenToClients();
	}

	private void createStreams() {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			errorHandler(e);
		}
	}

	private void listenToClients() {
		while (listenToClient.get()) {
			try {
				Object obj = in.readObject();
				Command result = checkCommandObject(obj);
				
				if (result != null) {
					out.writeObject(result);
					out.flush();
				}
			} catch (EOFException e) { 
				Output.println("No connection to Client");
				stop();
			} catch (SocketException e) { 
				Output.println("No connection to Client");
				stop();
			} catch (ClassNotFoundException e) {
				errorHandler(e);
			}
			catch (IOException e) {
				errorHandler(e);
			} 
		}
	}

	private void closeSocket() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			errorHandler(e);
		}
	}	

	private void errorHandler(Exception e) {
		e.printStackTrace();
		stop();	
	}

	public void stop() {
		listenToClient.set(false);		
		closeSocket();
	}

	private Command checkCommandObject(Object cmd) {
		if (cmd instanceof LoginCommand) {
			return login((LoginCommand)cmd);
		} else if (cmd instanceof LogoutCommand) {
			return logout((LogoutCommand)cmd);
		} else if (cmd instanceof ListCommand) {
			return list((ListCommand)cmd);
		} else if (cmd instanceof CreateCommand) {
			return create((CreateCommand)cmd);
		} else if (cmd instanceof BidCommand) {
			return bid((BidCommand)cmd);
		} else if (cmd instanceof EndCommand) {
			end((EndCommand)cmd);
			return null;
		} 
		
		return null;
	}

	private Command login(LoginCommand cmd) {
		InetAddress clientHost = socket.getInetAddress();		
		boolean login = Main.getUserManagement().login(cmd.getUserName(), clientHost, cmd.getUdpPort());
		
		if (login) {
			cmd.setResult("Succesfully logged in as " + cmd.getUserName() + "!");
		} else {
			cmd.setError("User " + cmd.getUserName() + " is already logged in!");
		}
		
		return cmd;
	}

	private Command logout(LogoutCommand cmd) {
		boolean logout = Main.getUserManagement().logout(cmd.getUserName());
		
		if (logout) {
			cmd.setResult("Succesfully logged out as " + cmd.getUserName() + "!");
		} else {
			cmd.setError("You have to log in first!");
		}

		return cmd;
	}

	private Command list(ListCommand cmd) {
		String auctionsList = Main.getAuctionManagement().printOpenAuctions();
		cmd.setResult(auctionsList);
		
		return cmd;
	}

	private Command create(CreateCommand cmd) {
		try {
			Auction auction = Main.getAuctionManagement().createAuction(cmd.getUserName(), cmd.getDescription(), cmd.getDurationInSeconds());
			cmd.setResult(auction.printCreate());
		} catch (ServerException e) {
			cmd.setError(e.getMessage());
		}
		
		return cmd;
	}

	private Command bid(BidCommand cmd) {
		try {
			Auction auction = Main.getAuctionManagement().placeBid(cmd.getUserName(), cmd.getAuctionId(), cmd.getAmount());
			cmd.setResult(auction.printSuccessfullyBid());
		} catch (ServerException e) {
			cmd.setError(e.getMessage());
		}
		
		return cmd;
	}

	private void end(EndCommand cmd) {
		Main.getUserManagement().logout(cmd.getUserName());
		
		cmd.setResult("Close socket!");
		stop();

		return;
	}
}
