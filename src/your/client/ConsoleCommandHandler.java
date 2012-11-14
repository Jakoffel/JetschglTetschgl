package your.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import your.client.commands.BidCommandHandler;
import your.client.commands.CreateCommandHandler;
import your.client.commands.EndCommandHandler;
import your.client.commands.ListCommandHandler;
import your.client.commands.LoginCommandHandler;
import your.client.commands.LogoutCommandHandler;
import your.common.helper.Output;

public class ConsoleCommandHandler {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private LoginCommandHandler login;
	private LogoutCommandHandler logout;
	private ListCommandHandler list;
	private CreateCommandHandler create;
	private BidCommandHandler bid;
	private EndCommandHandler end;
	
	public void createStreams(Socket socket) {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			login = new LoginCommandHandler(in, out);
			logout = new LogoutCommandHandler(in, out);
			list = new ListCommandHandler(in, out);
			create = new CreateCommandHandler(in, out);
			bid = new BidCommandHandler(in, out);
			end = new EndCommandHandler(in, out);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeStreams() {
		if (in == null && out == null) {
			return;
		}
		try {
			in.close();
			out.close();
		} catch (EOFException e) {
			Output.println("No Server-Connection");
		} catch (SocketException e) {
			Output.println("No Server-Connection");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleCommand(String commandString) {
		login.handle(commandString);
		logout.handle(commandString);
		list.handle(commandString);
		create.handle(commandString);
		bid.handle(commandString);
		end.handle(commandString);
	}
	
}
