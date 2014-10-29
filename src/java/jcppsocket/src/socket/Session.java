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
	private Socket 			 session    = null;
	private DataInputStream  iStream 	= null;
	private DataOutputStream oStream 	= null;
	private int				 magicA		= 23;
	private int				 magicB		= 42;

	public Session(Socket session) throws IOException {
		this.session = session;
		this.iStream    = new DataInputStream(this.session.getInputStream());
		this.oStream	= new DataOutputStream(this.session.getOutputStream());
	}

	public Session(Socket session, final int magicA, final int magicB) 
			throws IOException {
		this.session = session;
		this.magicA  = magicA;
		this.magicB  = magicB;
	}

	public SocketMsg read() throws IOException {
		try {
			int init = iStream.readInt();
			if(init != magicA) {
				throw new IOException("Wrong message initializer " + init + " !");
			}

			SocketMsg inMsg = GenericDeserializer.deserialize(iStream);

			int exit = iStream.readInt();
			if(exit != magicB) {
				throw new IOException("Wrong message terminator " + exit + " !");
			}
			return inMsg;
		} catch(EOFException e) {
			return new LogOff();
		}
	}

	public <T extends SocketMsg> T read(Class<T> type) throws IOException {
		int init = iStream.readInt();
		if(init != magicA) {
			throw new IOException("Wrong message initializer " + init + " !");
		}

		SocketMsg inMsg = GenericDeserializer.deserialize(iStream);

		int exit = iStream.readInt();
		if(exit != magicB) {
			throw new IOException("Wrong message terminator " + exit + " !");
		}

		if (inMsg.getClass().isAssignableFrom(type)) {
			return (T)inMsg;
		} else {
			return null;
		}
	}

	public void write(SocketMsg msg) throws IOException {
		oStream.writeInt(magicB);
		msg.serialize(oStream);
		oStream.writeInt(magicA);
	}

	public SocketMsg query(SocketMsg msg) throws IOException {
		write(msg);
		return read();
	}

	public void close() throws IOException {
		write(new LogOff());
		session.close();
	}

}
