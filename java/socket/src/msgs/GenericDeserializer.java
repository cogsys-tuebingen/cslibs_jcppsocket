package msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class GenericDeserializer {
	public static SocketMsg deserialize(DataInputStream _in) throws IOException {
		long id   = _in.readLong();
		Hash hash = new Hash(256); 
		hash.deserialize(_in);
		int  type = _in.readInt();
		int  size = (int)_in.readLong();
		
		SocketMsg msg = null;
		if(DataType.isSequence(type, size)) {
			msg = DataType.getSeqMessage(type);
		} else {
			msg = DataType.getMessage(type);
		}
		
		if(msg != null)
			msg.deserialize(id, hash, type, size, _in);
		
		return msg;
	}
}
