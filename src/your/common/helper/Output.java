package your.common.helper;

public class Output {
	public static void h1(String msg) {
		System.out.println();
		System.out.println("***************************");
		System.out.println(msg);
		System.out.println("***************************");
		System.out.println();
	}
	
	public static void h2(String msg) {
		System.out.println();
		System.out.println(msg);
		System.out.println("---------------------------");
		System.out.println();
	}
	
	public static void println(String msg) {
		System.out.println(msg);
	}
	
	public static void printError(String msg) {
		System.err.println("ERROR: " + msg);
	}
}
