package your.common.rmi;

public interface BillingServer {
	BillingServerSecure login(String username, String password);
}
