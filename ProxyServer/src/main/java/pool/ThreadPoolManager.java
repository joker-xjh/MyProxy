package pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import constant.Properties;

public class ThreadPoolManager {
	
	private static ExecutorService pool = Executors.newFixedThreadPool(Properties.threadSize);
	
	public static void execute(Thread thread) {
		pool.execute(thread);
	}

}
