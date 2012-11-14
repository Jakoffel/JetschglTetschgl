package your.server.objects;

import java.net.InetAddress;

public class User {
	private String name;
	private boolean isLoggedIn = false;
	private int udpPort;
	private InetAddress clientHost;
	
	public User(String name) {
		super();
		this.name = name;
	}

	public User(String name, int udpPort, InetAddress clientHost) {
		super();
		this.name = name;
		this.udpPort = udpPort;
		this.clientHost = clientHost;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public synchronized void login() {
		isLoggedIn = true;
	}
	
	public synchronized void logout() {
		isLoggedIn = false;
	}
	

	public synchronized String getName() {
		return name;
	}
	
	public synchronized boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public synchronized int getUdpPort() {
		return udpPort;
	}
	
	public synchronized InetAddress getClientHost() {
		return clientHost;
	}

	public synchronized void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public synchronized void setClientHost(InetAddress clientHost) {
		this.clientHost = clientHost;
	}
	
	
}
