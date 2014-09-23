package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import msgs.Error;
import msgs.GenericDeserializer;
import msgs.SocketMsg;

public class Server extends Thread
{
	private ServerSocket    serverSocket;
	private ServerProcessor processor = null;

	public Server(int _port) throws IOException
	{
		serverSocket = new ServerSocket(_port);
		serverSocket.setSoTimeout(10000);
	}

	public void registerProcessor(ServerProcessor _sp) {
		processor = _sp;
	}

	public void deregisterProcessor() {
		processor = null;
	}

	private void processData(Socket _server) throws IOException {
		DataInputStream  in  = new DataInputStream(_server.getInputStream());
		DataOutputStream out = new DataOutputStream(_server.getOutputStream());

		try {
			SocketMsg inMsg = read(in);

			if(inMsg != null) {
				if(processor == null) {
					System.out.println(inMsg.toString());
					write(out, inMsg);					
				} else {
					SocketMsg outMsg = processor.process(inMsg);
					write(out, outMsg);					
				}  
			} else {
				Error err = new Error("Unsupported message type!");
				write(out, err);
			}
		} catch (IOException e) {
			e.printStackTrace();
			in.skip(in.available());
			Error err = new Error(e.getMessage());
			write(out, err);
		}
	}

	private void write(DataOutputStream out, SocketMsg outMsg)
			throws IOException {
		out.writeInt(42);
		outMsg.serialize(out);
		out.writeInt(23);
	}

	private SocketMsg read(DataInputStream in) throws IOException {
		int init = in.readInt();
		if(init != 23)
			throw new IOException("Wrong message initializer " + init + " !");

		SocketMsg inMsg = GenericDeserializer.deserialize(in);

		int exit = in.readInt();
		if(exit != 42) 
			throw new IOException("Wrong message terminator " + exit + " !");
		return inMsg;
	}

	/// ---- Thread Part ---- ///
	public void run()
	{
		System.out.println("Opened!");
		while(!interrupted())
		{
			try
			{
				Socket server = null;
				try {
					server = serverSocket.accept();
				} catch(SocketException s) {
					s.printStackTrace();
					break;
				} 

				server.setKeepAlive(true);
//				server.setSoTimeout(1000);
//				while(server.isConnected()) {
					processData(server);
//				}

				server.close();

			} catch(SocketTimeoutException s) {
				System.out.println("No data to receive!");
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Closed!");
	}

	public void interrupt() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}