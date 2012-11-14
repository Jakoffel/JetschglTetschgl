package your.common.commands;

public class CreateCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5166655489982451721L;
	private int durationInSeconds;
	private String description;
	
	public CreateCommand(String userName, int durationInSeconds, String description) {
		super(userName);
		
		this.durationInSeconds = durationInSeconds;
		this.description = description;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public String getDescription() {
		return description;
	}
}
