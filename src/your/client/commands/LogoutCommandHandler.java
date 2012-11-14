package your.client.commands;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import your.client.Main;
import your.common.commands.Command;
import your.common.commands.LogoutCommand;

public class LogoutCommandHandler extends BaseCommandHandler {

	public LogoutCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);
		
		commandName = "!logout";
		argsLength = 1;
	}

	@Override
	protected Command getCommandObject() {
		String currentUserName = Main.getClient().getCurrentUserName();
		
		return new LogoutCommand(currentUserName);
	}

	@Override
	protected String printUsage() {
		return "!logout";
	}

	@Override
	protected void resultSuccessHook(Command result) {
		Main.getClient().logout();
	}
}
