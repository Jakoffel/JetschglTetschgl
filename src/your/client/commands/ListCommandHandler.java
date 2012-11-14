package your.client.commands;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import your.common.commands.Command;
import your.common.commands.ListCommand;

public class ListCommandHandler extends BaseCommandHandler {

	public ListCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);

		commandName = "!list";
		loginRequired = false;
		argsLength = 1;
	}

	@Override
	protected Command getCommandObject() {
		return new ListCommand();
	}

	@Override
	protected String printUsage() {
		return "!list";
	}

}
