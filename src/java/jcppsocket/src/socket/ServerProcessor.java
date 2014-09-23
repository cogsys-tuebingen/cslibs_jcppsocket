package socket;

import msgs.SocketMsg;

public interface ServerProcessor {
	public SocketMsg process(SocketMsg _in);
}
