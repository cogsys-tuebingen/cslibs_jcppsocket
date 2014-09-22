package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SocketMsg {
	protected long id   = 0;
	protected Hash hash = new Hash(256);
	protected int  type = -1;
	protected int size =  0;
	
	public void serialize(DataOutputStream _out) throws IOException {
		_out.writeLong(id);
		hash.serialize(_out);
		_out.writeInt(type);
		_out.writeInt(size);
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		id   = _in.readLong();
		hash.deserialize(_in);
		type = _in.readInt();
		size = _in.readInt();
	}
	
	public void deserialize(final long _id, 
							final Hash _hash, 
						    final int _type, 
						    final int _size, 
						    DataInputStream _in) throws IOException {
		id   = _id;
		hash = _hash;
		type = _type;
		size = _size;
	}
		
	public String toString() {
		String s = id + " " + hash + " " + type + " " + size;
		return s;
	}
}
 