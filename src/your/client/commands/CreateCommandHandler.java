package your.client.commands;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import your.client.Main;
import your.common.commands.Command;
import your.common.commands.CreateCommand;
import your.common.helper.Output;

public class CreateCommandHandler extends BaseCommandHandler {

	public CreateCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);

		commandName = "!create";
		argsLength = 3;
	}
	
	@Override
	protected void splitCommand(String commandString) {
		//description part can contain " ", so we need a special split-function
		
		commandArgs = commandString.split(" ", 3);
		if (commandArgs.length < argsLength) {
			return;
		}		
		
		String desc = commandArgs[2].trim();
		String s[] = { commandArgs[0], commandArgs[1], desc};
		commandArgs = s;
	}

	@Override
	protected boolean checkCommandHook() {
		int duration;
		try {
			duration = Integer.parseInt(commandArgs[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		
		if (duration > 0) {
			return true;
		} else {
			Output.println("no negative numbers");
			return false;
		}
	}
	
	@Override
	protected Command getCommandObject() {
		String currentUserName = Main.getClient().getCurrentUserName();
		int durationInSeconds = Integer.parseInt(commandArgs[1]);
		String description = commandArgs[2];
		
		return new CreateCommand(currentUserName, durationInSeconds, description);
	}

	@Override
	protected String printUsage() {
		return "!create <duration> <description>";
	}

}
