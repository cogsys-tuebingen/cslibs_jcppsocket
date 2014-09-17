package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Double extends SocketMsg {
	private double value = 0.0;
		
	public Double() {
		super.size = 8;
		super.type = DataType.double_t;
	}
	
	public Double(double _value) {
		value = _value;
		super.size = 8;
		super.type = DataType.double_t;
	}
	
	public Double(double _value, long _id) {
		value = _value;
		super.id = _id;
		super.size = 8;
		super.type = DataType.double_t;
	}
	
	public Double(double _value, long _id, Hash _hash) {
		value      = _value;
		super.id   = _id;
		super.hash = _hash;
		super.size = 8;
		super.type = DataType.double_t;
	}
	
	public Double( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 8;
		super.type = DataType.double_t;
	}
	
	void set(double _value) {
		value = _value;
	}
	
	double get() {
		return value;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeDouble(value);
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		value = _in.readDouble();
	}
	
	public void deserialize(long _id, Hash _hash, int _type, long _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		value = _in.readDouble();
	}
	
	public String toString() {
		return super.toString() + " [ " + value + " ]";
	}
	
}
