package your.common.commands;

public class LoginCommand extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8774512610826780052L;
	private int udpPort;
	
	public LoginCommand(String userName, int udpPort) {
		super(userName);
		
		this.udpPort = udpPort;
	}
	
	public int getUdpPort() {
		return udpPort;
	}
}
