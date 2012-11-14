package your.client.commands;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import your.client.Main;
import your.common.commands.Command;
import your.common.commands.LoginCommand;
import your.common.helper.Output;

public class LoginCommandHandler extends BaseCommandHandler {

	public LoginCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);
		
		commandName = "!login";
		argsLength = 2;
		loginRequired = false;
	}

	@Override
	protected boolean checkCommandHook() {
		if(Main.getClient().isUserLoggedIn()) {
			Output.println("User " + Main.getClient().getCurrentUserName() + " is already logged in");
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected String printUsage() {
		return "!login <username>";
	}


	@Override
	protected Command getCommandObject() {
		String userName = commandArgs[1];
		int udpPort = Main.getClient().getClientUdpPort();
		
		return new LoginCommand(userName, udpPort);
	}

	@Override
	protected void resultSuccessHook(Command result) {
		Main.getClient().login(result.getUserName());
	}
}
