package your.common.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {
	private static ExecutorService cachedThreadPool;
	
	private static void checkExecutorService() {
		if (cachedThreadPool == null) {
			cachedThreadPool = Executors.newCachedThreadPool();
		}
	}
	
	public static void execute(Runnable command) {
		checkExecutorService();
		cachedThreadPool.execute(command);
	}
	
	public static void shutdown() {
		checkExecutorService();
		cachedThreadPool.shutdown();		
	}
}
