package your.common.commands;

import java.io.Serializable;

public abstract class Command implements Serializable {
	
	private static final long serialVersionUID = 2362836012071945580L;
	
	protected String userName = "";
	protected String result = "";
	protected String error = "";
	
	public Command(String userName) {
		this.userName = userName;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public boolean failed() {
		return error.length() > 0;
	}
}
