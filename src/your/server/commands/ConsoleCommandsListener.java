package your.server.commands;

import java.util.Scanner;

import your.server.Main;

public class ConsoleCommandsListener {
	
	public void run() {
		Scanner in = new Scanner(System.in);
		in.nextLine();
		
		Main.getServer().stop();
	}
}