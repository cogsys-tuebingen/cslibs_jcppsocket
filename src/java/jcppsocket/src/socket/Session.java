package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import msgs.GenericDeserializer;
import msgs.LogOff;
import msgs.SocketMsg;

public class Session {
	private Socket 			 connection = null;
	private DataInputStream  iStream 	= null;
	private DataOutputStream oStream 	= null;

	public Session(Socket connection) throws IOException {
		this.connection = connection;
		this.iStream    = new DataInputStream(this.connection.getInputStream());
		this.oStream	= new DataOutputStream(this.connection.getOutputStream());
	}

	public SocketMsg read() throws IOException {
		try {
			int init = iStream.readInt();
			if(init != 23)
				throw new IOException("Wrong message initializer " + init + " !");

			SocketMsg inMsg = GenericDeserializer.deserialize(iStream);

			int exit = iStream.readInt();
			if(exit != 42) 
				throw new IOException("Wrong message terminator " + exit + " !");
			return inMsg;
		} catch(EOFException e) {
			return new LogOff();
		}
	}

	public void write(SocketMsg msg) throws IOException {
		oStream.writeInt(42);
		msg.serialize(oStream);
		oStream.writeInt(23);
	}

	void close() throws IOException {
		connection.close();
	}

}
