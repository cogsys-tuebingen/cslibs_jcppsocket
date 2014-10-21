package msgs;

import java.io.DataInputStream;
import java.io.IOException;

public class GenericDeserializer {
	public static SocketMsg deserialize(DataInputStream in) throws IOException {
		long id   	 = in.readLong();
		Hash hash    = new Hash(256); 
		hash.deserialize(in);
		int  type 	 = in.readInt();
		int	 dataOrg = in.readInt();
		int  size 	 = in.readInt();
		
		SocketMsg msg = null;
		switch(dataOrg) {
		case DataType.SINGLE_DO:
			msg = DataType.getMessage(type);
			break;
		case DataType.SEQUENCE_DO:
			msg = DataType.getSeqMessage(type);
			break;
		case DataType.BLOCK_DO:
			msg = DataType.getBlockMessage(type);
			break;		
		}
			
		if(msg != null)
			msg.deserialize(id, hash, type, dataOrg, size, in);
		else 
			System.err.println("Decoding error!");
		
		return msg;
	}
}
