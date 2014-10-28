package socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {
	private ThreadPoolExecutor executionPool;
	private int				   maxThreads;
	
	public ThreadPool(final int maxThreads) {
		this.executionPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads);
		this.maxThreads    = maxThreads;
	}
	
	public boolean execute(Runnable r) {
		if(executionPool.getActiveCount() == maxThreads)
			return false;
		
		executionPool.execute(r);
		return true;
	}
	
	public void shutdown() {
		executionPool.shutdown();
	}
	
	
}
