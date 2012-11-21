package your.common.rmi.exceptions;

import java.rmi.RemoteException;

public class BillingServerException extends RemoteException {

	private static final long serialVersionUID = 1L;

	private String msg;
	
	public BillingServerException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
