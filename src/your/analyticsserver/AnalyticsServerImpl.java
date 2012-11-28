package your.analyticsserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import your.common.rmi.AnalyticsServer;
import your.common.rmi.NotificationCallback;
import your.common.rmi.RmiHostPort;
import your.common.rmi.events.*;
import java.util.List;

public class AnalyticsServerImpl implements AnalyticsServer {
	
	private RmiHostPort rhp;
	private List<Event> eventList;
	private List<UserSubscription> userSubscriptionList;
	private long timestamp;
	
	
	
	public AnalyticsServerImpl() {
		rhp = new RmiHostPort();
		userSubscriptionList = Collections.synchronizedList(new ArrayList<UserSubscription>());
		eventList = Collections.synchronizedList(new ArrayList<Event>());
		timestamp = new Date().getTime();
	}

	public int getRmiPort() {
		return rhp.getPort();
	}

	public void run() {
		Scanner in = new Scanner(System.in);
		while (!in.nextLine().equals("!exit")) {}	
	}

	@Override
	public int subscribe(String filter, NotificationCallback callback)
			throws RemoteException {
		
		UserSubscription newUser = new UserSubscription(filter, callback);
		userSubscriptionList.add(newUser);
		int output = newUser.getSubscriptionID();

		return output;
	}


	@Override
	public void processEvent(Event event) throws RemoteException {
		eventList.add(event);
		if(event.getType().equals("AUCTION_ENDED")) {createAuctionSuccessRatioEvent(); createAuctionAvgTimeEvent();}
		else if(event.getType().equals("USER_LOGOUT")) {createUserSessionTimeEvent();}
		else if(event.getType().equals("USER_DISCONNECTED")) {createUserSessionTimeEvent();}
		else if(event.getType().equals("BID_PLACED")) {createBidStatisticsEvent();}
		
		
		for(int i=0;i<userSubscriptionList.size();i++) {
			if(event.getType().matches(userSubscriptionList.get(i).getFilter())) {
				userSubscriptionList.get(i).sendNotification(event.toString());
			}
		}
	}
	
	private void createBidStatisticsEvent() {
			long serverTime = new Date().getTime() - timestamp; 
			double bidPriceMax=0;
			Event chosenEvent;
			double price=0;
			int bidcounter=0;
			
			for (int i=0;i<eventList.size();i++) {
			chosenEvent=eventList.get(i);
			if(chosenEvent instanceof BidEvent) {
				price = ((BidEvent) chosenEvent).getPrice();
				if(price>bidPriceMax) {
					bidPriceMax=price;
				}
				bidcounter++;
			}
			}
			
			serverTime /= 60000;
			double bidCountPerMinute=bidcounter/serverTime;
			
			StatisticsEvent newEvent = new StatisticsEvent("BID_PRICE_MAX", bidPriceMax);
			StatisticsEvent newEvent2 = new StatisticsEvent("BID_COUNT_PER_MINUTE", bidCountPerMinute);
			eventList.add(newEvent);
			eventList.add(newEvent2);
			
	}
	

	private void createUserSessionTimeEvent() {
			double sessionAvg=0;
			long sessionMin=999999999;
			long sessionMax=0;
			long allSessionTime=0;
			int sessionCounter=0;
			
			for(int i=0;i<eventList.size();i++) {
				if(eventList.get(i).getType().equals("USER_LOGOUT") || eventList.get(i).getType().equals("USER_DISCONNECTED")) {
					for(int j=0;j<eventList.size();i++) {
						UserEvent logoutEvent=(UserEvent) eventList.get(i);
						
						if(eventList.get(j).getType().equals("USER_LOGIN")) {
							UserEvent loginEvent=(UserEvent) eventList.get(j);
							if(loginEvent.getSessionID()==logoutEvent.getSessionID()) {
								
								long sessionTime=logoutEvent.getTimeStamp()-loginEvent.getTimeStamp();
								allSessionTime += sessionTime;
								sessionCounter++;
								
								if(sessionTime<sessionMin) {
									sessionMin=sessionTime;
								}
								if(sessionTime>sessionMax) {
									sessionMax=sessionTime;
								}
							}
							
						}
					}
				} 
				
			}
			
			sessionAvg = allSessionTime/sessionCounter;
			
			StatisticsEvent newEvent = new StatisticsEvent("USER_SESSIONTIME_MIN", sessionMin);
			StatisticsEvent newEvent2 = new StatisticsEvent("USER_SESSION_MAX", sessionMax);
			StatisticsEvent newEvent3 = new StatisticsEvent("USER_SESSION_AVG", sessionAvg);
			eventList.add(newEvent);
			eventList.add(newEvent3);
			eventList.add(newEvent2);
			
	}
	
	private void createAuctionAvgTimeEvent() {
			//ArrayList<AuctionEvent> endedAuctionEventList = new ArrayList<AuctionEvent>();
			//ArrayList<AuctionEvent> startedAuctionEventList = new ArrayList<AuctionEvent>();
			long start=0;
			long end=0;
			long counter=0;
			long value=0;
			
			for(int i=0;i<eventList.size();i++) {
				if(eventList.get(i).getType().equals("AUCTION_ENDED")) {
					AuctionEvent chosenEndedEvent = (AuctionEvent) eventList.get(i);
					end+=chosenEndedEvent.getTimeStamp();
					counter++;
					
					long chosenAuctionID = chosenEndedEvent.getAuctionID();
					
					for(int j=0; j<eventList.size();j++) {
					if(eventList.get(j).getType().equals("AUCTION_STARTED")) {
					AuctionEvent chosenStartEvent = (AuctionEvent) eventList.get(j);
					if(chosenStartEvent.getAuctionID()==chosenAuctionID)
						start+=chosenStartEvent.getTimeStamp();
					}
				}
				}
			}
			
			value = (start-end)/counter;

			/*for(int i=0;i<endedAuctionEventList.size();i++) {
				AuctionEvent checkEvent = endedAuctionEventList.get(i);
				if(checkEvent.getType().equals("AUCTION_STARTED")) {
					start += checkEvent.getTimeStamp();
				}
				
				if(checkEvent.getType().equals("AUCTION_ENDED")) {
					end += checkEvent.getTimeStamp();
				}
				
				value = (start-end)/(endedAuctionEventList.size()/2);
			}
			*/
			StatisticsEvent newEvent = new StatisticsEvent("AUCTION_TIME_AVG", value);
			eventList.add(newEvent);
	}
	
	private void createAuctionSuccessRatioEvent() {

		double ratio=0;
		int endedAuctionsCounter=0;
		int successfulEndedAuctionsCounter=0;
		
		for(int i=0; i<eventList.size();i++) {
			if(eventList.get(i).getType().equals("AUCTION_ENDED")) {
				endedAuctionsCounter++;
			}
			if(eventList.get(i).getType().equals("BID_WON")) {
				successfulEndedAuctionsCounter++;
			}
		}
		ratio = endedAuctionsCounter/successfulEndedAuctionsCounter;

		StatisticsEvent newEvent = new StatisticsEvent("AUCTION_SUCCESS_RATIO", ratio);
		eventList.add(newEvent);
		
	}
	
	
	
	
	

	@Override
	public void unsubscribe(int subscriptionID) throws RemoteException {

		for (int i=0;i<userSubscriptionList.size();i++) {
			if(userSubscriptionList.get(i).getSubscriptionID()==subscriptionID) {
				userSubscriptionList.remove(i);
			}
		}
		
	}
}
