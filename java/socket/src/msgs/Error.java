package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Error extends SocketMsg {
	private String msg = "";
		
	public Error() {
		super.size = 0;
		super.type = DataType.error_t;
	}
	
	public Error(String _msg) {
		msg	   	   = _msg;
		super.type = DataType.error_t;
		set(_msg);
	}
	
	public Error(String _msg, long _id) {
		msg        = _msg;
		super.id   = _id;
		super.size = _msg.length();
		super.type = DataType.error_t;
	}
	
	public Error(String _msg, long _id, Hash _hash) {
		msg        = _msg;
		super.id   = _id;
		super.hash = _hash;
		super.type = DataType.error_t;
		set(_msg);
	}
	
	public Error( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.error_t;
	}
	
	void set(String _msg) {
		msg 	   =  "Error : '" + _msg + "'";
		super.size = msg.length();
	}
	
	String get() {
		return msg;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeBytes(msg);
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		
		readString(_in);
	}

	private void readString(DataInputStream _in) throws IOException {
		ArrayList<Byte> buff = new ArrayList<Byte>();
		for(int i = 0 ; i < size ; ++i) {
			buff.add(_in.readByte());
		}
		
		msg = String.valueOf(buff.toArray());
	}
	
	public void deserialize(long _id, Hash _hash, int _type, long _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		readString(_in);
	}
	
	public String toString() {
		return super.toString() + " [ " + msg + "]";
	}
}
