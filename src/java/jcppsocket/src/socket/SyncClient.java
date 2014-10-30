package socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import msgs.SocketMsg;

public class SyncClient {
	private String serverName;
	private int    serverPort;
	private Session session = null;

	public SyncClient(final String serverName, 
			final int	   serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
	}

	public boolean connect() {
		if(session == null) {
			try {
				Socket socket = new Socket(serverName, serverPort);
				session = new Session(socket, 42, 23);
			} catch (UnknownHostException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	public boolean disconnect() {
		if(session == null) {
			return false;
		}


		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
			session = null;
			return false;
		}

		session = null;
		return true;
	}

	public SocketMsg query(SocketMsg out) {
		if(session == null) {
			return new msgs.Error("Socket not connected!");
		}

		try {
			return session.query(out);
		} catch (IOException e) {
			return new msgs.Error(e.toString());
		}
	}

    public SocketMsg read() {
        if (session == null) {
            return new msgs.Error("Socket not connected!");
        }

        try {
            return session.read();
        } catch (IOException e) {
            return new msgs.Error(e.toString());
        }
    }

    public boolean write(SocketMsg msg) {
        if (session == null) {
            return false;
        }

        try {
            session.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

	public boolean isConnected() {
		if(session == null) {
			return false;
		}

		return true;
	}
}
