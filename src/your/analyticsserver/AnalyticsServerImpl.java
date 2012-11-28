package your.analyticsserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import your.common.rmi.AnalyticsServer;
import your.common.rmi.NotificationCallback;
import your.common.rmi.RmiHostPort;
import your.common.rmi.events.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalyticsServerImpl implements AnalyticsServer {
	
	private RmiHostPort rhp;
	private List<Event> eventList;
	private List<ManagementUser> managementUserList;
	
	
	public AnalyticsServerImpl() {
		rhp = new RmiHostPort();
		managementUserList = Collections.synchronizedList(new ArrayList<ManagementUser>());
		eventList = Collections.synchronizedList(new ArrayList<Event>());
	}

	public int getRmiPort() {
		return rhp.getPort();
	}

	public void run() {
		
	}

	@Override
	public int subscribe(String filter, NotificationCallback callback)
			throws RemoteException {
		
		ManagementUser newUser = new ManagementUser(filter, callback);
		managementUserList.add(newUser);
		int output = newUser.getUserId();

		return output;
	}


	@Override
	public void processEvent(Event event) throws RemoteException {
		eventList.add(event);		
	}


	@Override
	public void unsubscribe(int userId) throws RemoteException {

		for (int i=0;i<managementUserList.size();i++) {
			if(managementUserList.get(i).getUserId()==userId) {
				managementUserList.remove(i);
			}
		}
		
	}
}
