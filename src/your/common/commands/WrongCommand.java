package your.common.commands;

public class WrongCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8096891630913911366L;

	public WrongCommand(String userName) {
		super(userName);
	}
	
	@Override
	public String getResult() {
		return "Wrong command";
	}

}
