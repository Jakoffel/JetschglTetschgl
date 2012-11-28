package your.common.rmi.events;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class Event {

	private static AtomicInteger idCounter = new AtomicInteger(0);

	protected int id;
	protected String type;
	protected long time;

	public Event(String t) {
		type = t;
		assignNewUniqueId();
		time = new Date().getTime();

	}

	private void assignNewUniqueId() {
		id = idCounter.incrementAndGet();
	}

	public String getType() {
		return type;
	}
	
	public int getID() {
		return id;
	}
	
	public long getTimeStamp() {
		return time;
	}

}
