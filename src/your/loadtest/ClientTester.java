package your.loadtest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import your.common.commands.BidCommand;
import your.common.commands.CreateCommand;
import your.common.commands.ListCommand;
import your.common.commands.LoginCommand;
import your.common.helper.Output;

public class ClientTester {
	private static AtomicInteger clientCounter = new AtomicInteger(0);
	private String userName;

	private int auctionsPerMin;
	private int auctionDuration;
	private int updateIntervalSec;
	private int bidsPerMin;
	private String serverHost;
	private int serverPort;

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Timer timer;
	private TimerTask timerTaskMinute;
	private TimerTask timerTaskListUpdate;

	private List<Integer> auctionIds;

	public ClientTester(int auctionsPerMin, int auctionDuration,
			int updateIntervalSec, int bidsPerMin, String serverHost,
			int serverPort) {
		super();
		this.auctionsPerMin = auctionsPerMin;
		this.auctionDuration = auctionDuration;
		this.updateIntervalSec = updateIntervalSec;
		this.bidsPerMin = bidsPerMin;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		userName = "c" + clientCounter.incrementAndGet();

		timer = new Timer();
		timerTaskMinute = getTimerTaskMinute();
		timerTaskListUpdate = getTimerTaskListUpdate();
		
		auctionIds = Collections.synchronizedList(new ArrayList<Integer>());
	}

	private TimerTask getTimerTaskMinute() {
		return new TimerTask() {
			@Override
			public void run() {
				//Auktionen starten
				try {
					for (int i = 0; i < auctionsPerMin; i++) {
						CreateCommand create = new CreateCommand(userName, auctionDuration, "auction from "+userName);
						out.writeObject(create);
						out.flush();
						create = (CreateCommand) in.readObject();
						if (create.failed()) {
							Output.printError(create.getError());
						}
					}
				} catch(IOException e) {
					Output.printError("create auctions");
				} catch (ClassNotFoundException e) {
					Output.printError("get create result");
				}
				
				//Gebote platzieren				
				if (auctionIds.size() > 0) {
					try {
						for (int i = 0; i < bidsPerMin; i++) {
							int auctionId = getRandomAuctionId();
							BidCommand bid = new BidCommand(userName, auctionId, Main.getBid());
							out.writeObject(bid);
							out.flush();
							bid = (BidCommand) in.readObject();
							if (bid.failed()) {
								Output.printError(bid.getError());
							}
						}
					} catch(IOException e) {
						Output.printError("create auctions");
					} catch (ClassNotFoundException e) {
						Output.printError("get bid result");
					}
				}
			}
		};
	}

	private TimerTask getTimerTaskListUpdate() {
		return new TimerTask() {
			
			@Override
			public void run() {
				try {
					ListCommand list = new ListCommand();
					out.writeObject(list);
					out.flush();
					list = (ListCommand) in.readObject();
					if (list.failed()) {
						Output.printError(list.getError());
					} else {
						ArrayList<Integer> newIds = new ArrayList<Integer>();
						String lines[] = list.getResult().split("\n");
						for (String line : lines) {
							String idString = line.split(" ")[0];
							int id = Integer.parseInt(idString.substring(0, idString.length()-1));
							newIds.add(id);
						}
						
						updateIds(newIds);
					}
				} catch(IOException e) {
					Output.printError("get auction list");
				} catch (ClassNotFoundException e) {
					Output.printError("get list result");
				}
				
			}
		};
	}

	protected synchronized int getRandomAuctionId() {
		Random random = new Random();
		return auctionIds.get(random.nextInt(auctionIds.size()));
	}

	protected synchronized void updateIds(ArrayList<Integer> newIds) {
		auctionIds.clear();
		auctionIds.addAll(newIds);
	}

	public void run() {
		try {
			createStreams();
			login();
			timer.schedule(timerTaskMinute, 500, 60000);
			timer.schedule(timerTaskListUpdate, 500, updateIntervalSec*1000);
			
		} catch (SocketException e) {
			Output.printError("connection lost");
		} catch (EOFException e) {
			Output.printError("connection lost");
		} catch (UnknownHostException e) {
			Output.printError("Unknown serverhost");
		} catch (IOException e) {
			Output.printError("IOException");
		} catch (ClassNotFoundException e) {
			Output.printError("ClassNotFoundException");
		} 
	}
	
	private void createStreams() throws UnknownHostException, IOException {
		socket = new Socket(serverHost, serverPort);
		out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		out.flush();
		in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
	}

	private void login() throws IOException, ClassNotFoundException {
		LoginCommand login = new LoginCommand(userName, 1);
		out.writeObject(login);
		out.flush();
		login = (LoginCommand) in.readObject();
		if (login.failed()) {
			Output.printError(login.getError());
		}
	}
	
	public void stop() {
		try {
			timer.cancel();
			
			if (socket != null) {
				socket.close();
			}

			if (in != null) {
				in.close();
			}

			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			Output.printError("closing streams/stock");
		}
	}	
}
