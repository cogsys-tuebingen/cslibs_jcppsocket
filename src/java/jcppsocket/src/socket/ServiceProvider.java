package socket;

import msgs.SocketMsg;

public interface ServiceProvider {
	public SocketMsg process(SocketMsg _in);
}
