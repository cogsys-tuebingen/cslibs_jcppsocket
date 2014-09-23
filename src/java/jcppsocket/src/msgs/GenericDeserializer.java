package msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class GenericDeserializer {
	public static SocketMsg deserialize(DataInputStream _in) throws IOException {
		long id   	 = _in.readLong();
		Hash hash    = new Hash(256); 
		hash.deserialize(_in);
		int  type 	 = _in.readInt();
		int	 dataOrg = _in.readInt();
		int  size 	 = _in.readInt();
		
		SocketMsg msg = null;
		switch(dataOrg) {
		case DataType.single_do:
			msg = DataType.getMessage(type);
			break;
		case DataType.sequence_do:
			msg = DataType.getSeqMessage(type);
			break;
		case DataType.block_do:
			msg = DataType.getBlockMessage(type);
			break;		
		}
				
		if(msg != null)
			msg.deserialize(id, hash, type, dataOrg, size, _in);
		
		return msg;
	}
}
