package your.client.commands;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import your.client.Main;
import your.common.commands.Command;
import your.common.helper.MyThreadPool;
import your.common.helper.Output;

public abstract class BaseCommandHandler {
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	
	protected String commandName = "";
	protected int argsLength;
	protected boolean loginRequired = true;
	protected boolean readResultObject = true;
	
	protected String[] commandArgs;
	
	public BaseCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super();
		this.in = in;
		this.out = out;
	}

	protected abstract Command getCommandObject();
	protected abstract String printUsage();
	
	protected void handleHook() {}
	protected void splitCommand(String commandString) {
		commandArgs = commandString.split(" ");
	}
	public void handle(String commandString) {
		splitCommand(commandString);		
		
		if (!checkCommandString(commandString)) {
			return;
		}
		
		handleHook();
		
		sendCommand();
	}

	protected boolean checkCommandHook() {return true;}
	
	private boolean checkCommandString(String commandString) {
		if (!commandString.contains(commandName)) {
			return false;
		}
		
		if (loginRequired && !Main.getClient().isUserLoggedIn()) {
			Output.println("You have to login first!");
			return false;
		}
		
		
		if (commandArgs.length != argsLength) {
			Output.println("Usage: " + printUsage());
			return false;
		}
		
		if (!checkCommandHook()) {
			return false;
		}
		
		return true;
	}

	protected void sendCommand() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					Command command = getCommandObject();
					out.writeObject(command);
					out.flush();

					if (readResultObject) {
						Command result = (Command)in.readObject();
						handleResult(result);
					} 
				} catch (EOFException e) {
					Output.println("No Serverconnection!");
				} catch (SocketException e) {
					Output.println("No Serverconnection!");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		};		

		MyThreadPool.execute(r);
	}

	protected void resultFailedHook(Command result) {}
	protected void resultSuccessHook(Command result) {}
	private void handleResult(Command result) {
		if (result.failed()) {
			resultFailedHook(result);
			Output.println(result.getError());
		} else {
			resultSuccessHook(result);
			Output.println(result.getResult());
		}
	}
}
