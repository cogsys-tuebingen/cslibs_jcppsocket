package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FloatSeq extends SocketMsg {
	private ArrayList<java.lang.Float> seq = new ArrayList<java.lang.Float>();
		
	public FloatSeq() {
		super.size = 0;
		super.type = DataType.float_t;
	}
	
	public FloatSeq(ArrayList<java.lang.Float> _seq) {
		seq = new ArrayList<java.lang.Float>();
		super.size = 4 * seq.size();
		super.type = DataType.float_t;
	}
	
	public FloatSeq(ArrayList<java.lang.Float> _seq, long _id) {
		seq = new ArrayList<java.lang.Float>();
		super.id = _id;
		super.size = 4 * seq.size();
		super.type = DataType.float_t;
	}
	
	public FloatSeq(ArrayList<java.lang.Float> _seq, long _id, Hash _hash) {
		seq = new ArrayList<java.lang.Float>();
		super.id   = _id;
		super.hash = _hash;
		super.size = 4 * seq.size();
		super.type = DataType.float_t;
	}
	
	public FloatSeq( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.float_t;
	}
	
	void set(ArrayList<java.lang.Float> _seq) {
		seq 	   = new ArrayList<java.lang.Float>();
		super.size = 4 * _seq.size();
	}
	
	ArrayList<java.lang.Float> get() {
		return seq;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		for (float f : seq) {
			_out.writeFloat(f);
		}
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		readSequence(_in);
	}

	public void deserialize(long _id, Hash _hash, int _type, long _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		readSequence(_in);
	}
	
	private void readSequence(DataInputStream _in) throws IOException {
		long  size = super.size / 4;
		seq = new ArrayList<java.lang.Float>();
		for(long l = 0 ; l < size ; ++l) {
			seq.add(_in.readFloat());
		}
	}
	
	public String toString() {
		String buff = super.toString() + " [ ";
		for(float f : seq) {
			buff += f + " ";
		}
		buff += "]";
		return buff;
	}
}
