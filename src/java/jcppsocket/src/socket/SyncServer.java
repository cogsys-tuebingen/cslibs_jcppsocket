package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class SyncServer extends Thread
{
	private ServerSocket     serverSocket;
	private ThreadPool		 threadPool;
	private ProviderFactory  providerFactory;


	public SyncServer(final int port, final int maxSessions) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.serverSocket.setSoTimeout(10000);
		this.threadPool   = new ThreadPool(maxSessions);

	}

	public void setProviderFactory(ProviderFactory fac) {
		providerFactory = fac;
	}

	public void resetProviderFactory() {
		providerFactory = null;
	}

	/// ---- Thread Part ---- ///
	public void run()
	{
		System.out.println("Services are available!");
		while(!interrupted())
		{
			try
			{
				Socket connection = null;
				try {
					connection = serverSocket.accept();
				} catch(SocketException s) {
					s.printStackTrace();
					break;
				} 

				if(providerFactory != null) {
					Session session = new Session(connection);
					if(!threadPool.execute(providerFactory.getInstance(session))) {
						System.err.println("Dropped connection!");
						session.close();
					}
				}
			} catch(SocketTimeoutException s) {
				System.out.println("No client approaches the service!");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Services are shut down!");
	}

	public void interrupt() {
		try {
			threadPool.shutdown();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}