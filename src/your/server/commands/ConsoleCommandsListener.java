package your.server.commands;

import java.util.Scanner;

import your.server.Main;

public class ConsoleCommandsListener {
	
	public void run() {
		Scanner in = new Scanner(System.in);
		while (!in.nextLine().equals("!end")) {}
		
		Main.getServer().stop();
	}
}
