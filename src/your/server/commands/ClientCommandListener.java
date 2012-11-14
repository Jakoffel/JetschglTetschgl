package your.server.commands;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import your.common.helper.MyThreadPool;
import your.common.helper.Output;

public class ClientCommandListener implements Runnable {

	private int tcpPort;
	public AtomicBoolean waitingForClients = new AtomicBoolean();
	private ServerSocket serverSocket;
	private ArrayList<ClientCommandHandler> clients;
	
	public ClientCommandListener(int tcpPort) {
		waitingForClients.set(true);
		this.tcpPort = tcpPort;
		clients = new ArrayList<ClientCommandHandler>();
	}
	
	@Override
	public void run() {
		createServerSocket();
		listenToServerSocket();
	}

	private void createServerSocket() {
		try {
			serverSocket = new ServerSocket(tcpPort);
		} catch (EOFException e) {
			Output.println("Can not open ServerSocket, port is already used");
			stopListening();
		} catch (SocketException e) {
			Output.println("Can not open ServerSocket, port is already used");
			stopListening();
		} catch (IOException e) {
			errorListener(e);
		}
	}

	private void listenToServerSocket() {
		if (waitingForClients.get()) {
			Output.h2("Open ServerSocket.");
		}
		
		while (waitingForClients.get()) {
			try {
				Socket socket = serverSocket.accept();
				ClientCommandHandler cch = new ClientCommandHandler(socket);
				clients.add(cch);
				
				MyThreadPool.execute(cch);
			} catch (SocketException e) {
				Output.println("ServerSocket clost.");
			} catch (IOException e) {
				errorListener(e);
			}	
		}
	}

	public void stopListening() {
		Output.h2("Close ServerSocket and clientconnections.");
		
		for (ClientCommandHandler cch : clients) {
			cch.stop();
		}
		
		waitingForClients.set(false);
        closeServerSocket();
	}

	private void closeServerSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			errorListener(e);
		}
	}
	
	private void errorListener(Exception e) {
		e.printStackTrace();
		stopListening();		
	}
}