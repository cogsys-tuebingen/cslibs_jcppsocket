package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Error extends SocketMsg {
	private String msg = "";
		
	public Error() {
		super.size = 0;
		super.type = DataType.ERROR_T;
	}
	
	public Error(final String msg) {
		this.msg	   	   = msg;
		super.type = DataType.ERROR_T;
		set(msg);
	}
	
	public Error(final String msg, long _id) {
		this.msg        = msg;
		super.id   = _id;
		super.size = msg.length();
		super.type = DataType.ERROR_T;
	}
	
	public Error(final String msg, 
				 final long   _id, 
				 final Hash   _hash) {
		this.msg        = msg;
		super.id   = _id;
		super.hash = _hash;
		super.type = DataType.ERROR_T;
		set(msg);
	}
	
	public Error(final long _id, 
				 final Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.ERROR_T;
	}
	
	public void set(String msg) {
		this.msg 	   =  "Error : '" + msg + "'";
		super.size = this.msg.length();
	}
	
	public String get() {
		return this.msg;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeBytes(this.msg);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		
		readString(in);
	}

	private void readString(DataInputStream in) throws IOException {
		ArrayList<Byte> buff = new ArrayList<Byte>();
		for(int i = 0 ; i < size ; ++i) {
			buff.add(in.readByte());
		}
		
		this.msg = String.valueOf(buff.toArray());
	}
	
	public void deserialize(final long 		id, 
							final Hash 		hash, 
						    final int  		type,
						    final int  		dataOrg,
						    final int  		size, 
						    DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		readString(in);
	}
	
	public String toString() {
		return super.toString() + " [ " + this.msg + "]";
	}
}
