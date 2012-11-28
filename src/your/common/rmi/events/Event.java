package your.common.rmi.events;

import java.util.concurrent.atomic.AtomicInteger;


public abstract class Event {

	private static AtomicInteger idCounter = new AtomicInteger(0);

	protected int id;
	protected String type;
	protected long time;

	public Event(String t, long ti) {
		type = t;
		assignNewUniqueId();
		time = ti;

	}

	private void assignNewUniqueId() {
		id = idCounter.incrementAndGet();
	}

}
