package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Int extends SocketMsg {
	private int value = 0;
		
	public Int() {
		super.size = 4;
	}
	
	public Int(int _value) {
		value = _value;
		super.size = 4;
		super.type = DataType.int_t;
	}
	
	public Int(int _value, long _id) {
		value = _value;
		super.id = _id;
		super.size = 4;
		super.type = DataType.int_t;
	}
	
	public Int(int _value, long _id, Hash _hash) {
		value      = _value;
		super.id   = _id;
		super.hash = _hash;
		super.size = 4;
		super.type = DataType.int_t;
	}
	
	public Int( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 4;
		super.type = DataType.int_t;
	}
	
	void set(int _value) {
		value = _value;
	}
	
	int get() {
		return value;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeInt(value);
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		value = _in.readInt();
	}
	
	public void deserialize(long _id, Hash _hash, int _type, int _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		value = _in.readInt();
		System.out.println(value);
	}
	
	public String toString() {
		return super.toString() + " [ " + value + " ]";
	}
}
