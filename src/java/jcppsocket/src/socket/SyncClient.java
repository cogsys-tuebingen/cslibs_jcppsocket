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
	private String 		     serverName;
	private int    			 serverPort;
	private Socket 			 socket   = null;
	private DataInputStream  diStream = null;
	private DataOutputStream doStream = null;
	
	public SyncClient(final String serverName, 
					  final int	   serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
	}

	public boolean connect() {
		if(socket == null) {
			try {
				socket   = new Socket(serverName, serverPort);
				diStream = new DataInputStream(socket.getInputStream());
				doStream = new DataOutputStream(socket.getOutputStream());
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
			LogOff logoff = new LogOff();
			write(logoff);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			socket   = null;
			doStream = null;
			diStream = null;
			return false;
		}

		doStream = null;
		diStream = null;
		socket = null;
		return true;
	}

	public SocketMsg query(SocketMsg out) {
		if(socket == null || !socket.isConnected()) {
			return new msgs.Error("Socket not connected!");
		}
		
		try {
			
			write(out);
			return read();
			
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
	
	private void write(SocketMsg outMsg)
			throws IOException {
		doStream.writeInt(23);
		outMsg.serialize(doStream);
		doStream.writeInt(42);
	}
	
	private SocketMsg read() throws IOException {
		int magicA = diStream.readInt();
		if(magicA != 42)
			throw new IOException("Wrong message initializer " + magicA + " !");

		SocketMsg inMsg = GenericDeserializer.deserialize(diStream);

		int magicB = diStream.readInt();
		if(magicB != 23) 
			throw new IOException("Wrong message terminator " + magicB + " !");
		
		return inMsg;
	}
}
