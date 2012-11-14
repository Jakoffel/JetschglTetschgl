package your.client;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import your.common.helper.Output;

public class ServerNotificationListener implements Runnable {

	private boolean isListening = true;
	private DatagramSocket socket;
	
	@Override
	public void run() {
		DatagramPacket packet;
        
		createSocket();
		
		while (isListening) {
			byte[] receiveData = new byte[200];
			packet = new DatagramPacket(receiveData, receiveData.length);
			
			try {
				socket.receive(packet);
				String msg = new String(packet.getData());
				printMsg(msg);
			} catch (SocketException e) {
				Output.println("Notification-Socket closed");
			} catch (EOFException e) {
				Output.println("Notification-Socket closed");
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
	}

	private void printMsg(String msg) {
		printNewBidNotification(msg);
		printAuctionEndNotification(msg);
	}

	private void printNewBidNotification(String msg) {
		if (!msg.contains("!new-bid")) {
			return;
		}
		
		String args[] = msg.split(" ", 2);
		String description = args[1].trim();
		
		Output.println("You have been overbid on " + description);
		
	}

	private void printAuctionEndNotification(String msg) {
		if (!msg.contains("!auction-ended")) {
			return;
		}
		
		String args[] = msg.split(" ", 4);
		String winner = args[1];
		String amount = args[2];
		String description = args[3].trim();
		
		Output.println("The auction '" + description + "' has ended. " + winner + " won with " + amount + "!");
	}

	private void createSocket() {
		try {
			socket = new DatagramSocket(Main.getClient().getClientUdpPort());
		} catch (SocketException e) {
			Output.println("Datagramsocket could not be opened, or the socket could not bind to the specified local port.");
			Main.getClient().shutdown();
		}
	}

	public void stop() {
		isListening = false;
		
		if (socket != null) {
			socket.close();
		}
	}
}
