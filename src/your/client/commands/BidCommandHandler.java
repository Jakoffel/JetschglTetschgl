package your.client.commands;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import your.client.Main;
import your.common.commands.BidCommand;
import your.common.commands.Command;
import your.common.helper.Output;

public class BidCommandHandler extends BaseCommandHandler {

	public BidCommandHandler(ObjectInputStream in, ObjectOutputStream out) {
		super(in, out);
		
		commandName = "!bid";
		argsLength = 3;		
	}

	@Override
	protected boolean checkCommandHook() {
		int auktionsId;
		try {
			auktionsId = Integer.parseInt(commandArgs[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		
		BigDecimal amount = null;
		try {
			DecimalFormat df = new DecimalFormat();
			df.setParseBigDecimal(true);
			amount = (BigDecimal) df.parse(commandArgs[2]);
		} catch (ParseException e) {
			return false;
		}
	
		if (amount.floatValue() > 0 && auktionsId > 0) {
			return true;
		} else {
			Output.println("no negative numbers");
			return false;
		}
	}
	
	@Override
	protected Command getCommandObject() {
		DecimalFormat df = new DecimalFormat();
		df.setParseBigDecimal(true);		
		BigDecimal amount = null;
		try {
			amount = (BigDecimal) df.parse(commandArgs[2]);
		} catch (ParseException e) {
			//checkCommandHook() guarantees, that here will never be a ParseException!!
			e.printStackTrace();
		}
		
		int auctionId = Integer.parseInt(commandArgs[1]);
		String currentUserName = Main.getClient().getCurrentUserName();
		
		return new BidCommand(currentUserName, auctionId, amount);
	}

	@Override
	protected String printUsage() {
		return "!bid <auction-id> <amount>";
	}
	
	

}
