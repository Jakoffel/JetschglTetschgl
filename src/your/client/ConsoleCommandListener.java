package your.client;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import your.common.helper.Output;

public class ConsoleCommandListener {

	private ConsoleCommandHandler consoleCommandHandler;
	
	private String serverHost;
	private int serverTcpPort;
	private boolean isListening = true;
	private Socket socket;
	private Scanner in;
	
	public ConsoleCommandListener(String serverHost, int tcpPort) {
		consoleCommandHandler = new ConsoleCommandHandler();
		in = new Scanner(System.in);
		this.serverHost = serverHost;
		this.serverTcpPort = tcpPort;
	}
	
	public void start() {
		createSocket();
		listenToCommands();
	}
	
	private void createSocket() {
		Output.h2("Connect to ServerSocket.");
		
		try {
			socket = new Socket(serverHost, serverTcpPort);
			consoleCommandHandler.createStreams(socket);
		} catch (EOFException e) {
			openSocketFailed();
		} catch (SocketException e) {
			openSocketFailed();
		} catch (UnknownHostException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		}
	}

	private void listenToCommands() {
		Output.println(Main.getClient().getCurrentUserName() + "> ");
		
		while(isListening) {
			String cmd = in.nextLine();
			
			consoleCommandHandler.handleCommand(cmd);
		}
	}

	public void stop() {
		Output.h2("Close connection to ServerSocket.");
		
		in.close();
		isListening = false;
		closeSocket();
	}
	
	private void closeSocket() {
		consoleCommandHandler.closeStreams();
		
		if (socket == null) {
			return;
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			error(e);
		}		
	}

	private void openSocketFailed() {
		isListening = false;
		Output.println("No connection to Server");
		Main.getClient().shutdown();
	}
	
	private void error(Exception e) {
		e.printStackTrace();
		stop();
	}
}
