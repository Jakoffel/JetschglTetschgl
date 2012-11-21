package your.common.rmi.exceptions;

import java.rmi.RemoteException;

public class AnalyticsServerException extends RemoteException {

	private static final long serialVersionUID = 1L;
	private String msg;

	public AnalyticsServerException(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return msg;
	}
}
