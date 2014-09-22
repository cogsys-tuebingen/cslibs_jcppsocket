package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Float extends SocketMsg {
	private float value = 0.f;

	public Float() {
		super.size = 4;
		super.type = DataType.float_t;
	}

	public Float(float _value) {
		value	   = _value;
		super.size = 4;
		super.type = DataType.float_t;
	}

	public Float(float _value, long _id) {
		value      = _value;
		super.id   = _id;
		super.size = 4;
		super.type = DataType.float_t;
	}

	public Float(float _value, long _id, Hash _hash) {
		value      = _value;
		super.id   = _id;
		super.hash = _hash;
		super.size = 4;
		super.type = DataType.float_t;
	}

	public Float( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 4;
		super.type = DataType.float_t;
	}

	void set(float _value) {
		value = _value;
	}

	float get() {
		return value;
	}

	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeFloat(value);
	}

	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		value = _in.readFloat();
	}

	public void deserialize(long _id, Hash _hash, int _type, int _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		value = _in.readFloat();
	}

	public String toString() {
		return super.toString() + " [ " + value + " ]";
	}
}
