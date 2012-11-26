package your.billingserver;

import java.util.Scanner;

public class ConsoleCommandsListener {
	
	public void run() {
		Scanner in = new Scanner(System.in);
		while (!in.nextLine().equals("!end")) {}	
	}
}
