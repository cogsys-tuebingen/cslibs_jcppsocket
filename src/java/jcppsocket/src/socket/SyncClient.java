package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import msgs.GenericDeserializer;
import msgs.SocketMsg;

public class SyncClient {
	private String serverName;
	private int    serverPort;
	private Socket socket = null;

	public SyncClient(final String serverName, 
					  final int	   serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
	}

	public boolean connect() {
		if(socket == null) {
			try {
				socket = new Socket(serverName, serverPort);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	public boolean disconnect() {
		if(socket == null) {
			return false;
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			socket = null;
			return false;
		}
		
		socket = null;
		return true;
	}

	public boolean query(SocketMsg out, SocketMsg in) {
		try {
			DataInputStream  inStr  = new DataInputStream(socket.getInputStream());
			DataOutputStream outStr = new DataOutputStream(socket.getOutputStream());
			
			write(outStr, out);
			read(inStr, in);
			
		} catch (IOException e) {
			in = new msgs.Error(e.toString());
			e.printStackTrace();
		}
		
		return true;
	}

	public boolean isConnected() {
		if(socket == null) {
			return false;
		}

		return this.socket.isConnected();
	}
	
	private void write(DataOutputStream out, SocketMsg outMsg)
			throws IOException {
		out.writeInt(23);
		outMsg.serialize(out);
		out.writeInt(42);
	}
	
	private void read(DataInputStream in, SocketMsg inMsg) throws IOException {
		int init = in.readInt();
		if(init != 42)
			throw new IOException("Wrong message initializer " + init + " !");

		inMsg = GenericDeserializer.deserialize(in);

		int exit = in.readInt();
		if(exit != 23) 
			throw new IOException("Wrong message terminator " + exit + " !");
		
	}




}
