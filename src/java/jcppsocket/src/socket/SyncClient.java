package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import msgs.Error;
import msgs.GenericDeserializer;
import msgs.LogOff;
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
				return false;
			} catch (IOException e) {
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
			DataOutputStream outStr = new DataOutputStream(socket.getOutputStream());
			write(outStr, new LogOff());
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			socket = null;
			return false;
		}
		
		socket = null;
		return true;
	}

	public SocketMsg query(SocketMsg out) {
		if(socket == null || !socket.isConnected()) {
			return new msgs.Error("Socket not connected!");
		}
		
		try {
			DataInputStream  inStr  = new DataInputStream(socket.getInputStream());
			DataOutputStream outStr = new DataOutputStream(socket.getOutputStream());
			
			write(outStr, out);
			return read(inStr);
			
		} catch (IOException e) {
			return new msgs.Error(e.toString());
		}
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
	
	private SocketMsg read(DataInputStream in) throws IOException {
		int init = in.readInt();
		if(init != 42)
			throw new IOException("Wrong message initializer " + init + " !");

		SocketMsg inMsg = GenericDeserializer.deserialize(in);

		int exit = in.readInt();
		if(exit != 23) 
			throw new IOException("Wrong message terminator " + exit + " !");
		
		return inMsg;
	}
}
