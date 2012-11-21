package your.common.rmi;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import your.common.helper.Output;

public class RmiHostPort {
	private int port;
	private String host;
	
	public RmiHostPort() {
		Properties pro = new Properties();
		try{
			java.io.InputStream in = ClassLoader.getSystemResourceAsStream("registry.properties");
			pro.load(in);
			Enumeration<Object> em = pro.keys();
			
			String key = (String) em.nextElement();
			port = Integer.parseInt(pro.getProperty(key));
			
			key = (String) em.nextElement();
			host = pro.getProperty(key);
		} catch(IOException e){
			Output.println("Error @ reading registry properties");
		}
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}
}
