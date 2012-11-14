package your.client.commands;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import your.client.Main;
import your.common.commands.Command;
import your.common.commands.EndCommand;
import your.common.helper.Output;

public class EndCommandHandler extends BaseCommandHandler {

	public EndCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);

		commandName = "!end";
		loginRequired = false;
		argsLength = 1;
		readResultObject = false;
	}

	@Override
	protected Command getCommandObject() {
		String currentUserName = Main.getClient().getCurrentUserName();
		
		return new EndCommand(currentUserName);
	}

	@Override
	protected String printUsage() {
		return "!end";
	}
	
	@Override
	protected void sendCommand() {
		//no need for thread for Client-Shutdown
		//when server is not available, Shutdown of Client
		
		Output.println("Client-Shutdown");
		
		try {
			Command command = getCommandObject();
			out.writeObject(command);
			out.flush();
		} catch (EOFException e) {
			//Server not available, because Client shuts down, 
			//we need no Exception-Handling
		} catch (SocketException e) {
			//Server not available, because Client shuts down, 
			//we need no Exception-Handling
		} catch (IOException e) {
			e.printStackTrace();
		} 

		Main.getClient().shutdown();
	}
}
