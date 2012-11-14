package your.server.exceptions;

public class ServerException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;
	
	public ServerException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}
}
