package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class IntSeq extends SocketMsg {
	private ArrayList<java.lang.Integer> seq = new ArrayList<Integer>();
		
	public IntSeq() {
		super.size = 0;
		super.type = DataType.int_t;
	}
	
	public IntSeq(ArrayList<java.lang.Integer> _seq) {
		seq = new ArrayList<java.lang.Integer>(_seq);
		super.size = 4 * seq.size();
		super.type = DataType.int_t;
	}
	
	public IntSeq(ArrayList<java.lang.Integer> _seq, long _id) {
		seq = new ArrayList<java.lang.Integer>(_seq);
		super.id = _id;
		super.size = 4 * seq.size();
		super.type = DataType.int_t;
	}
	
	public IntSeq(ArrayList<java.lang.Integer> _seq, long _id, Hash _hash) {
		seq = new ArrayList<java.lang.Integer>(_seq);
		super.id   = _id;
		super.hash = _hash;
		super.size = 4 * seq.size();
		super.type = DataType.int_t;
	}
	
	public IntSeq( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.int_t;
	}
	
	void set(ArrayList<java.lang.Integer> _seq) {
		seq 	   = new ArrayList<java.lang.Integer>(_seq);
		super.size = 4 * _seq.size();
	}
	
	ArrayList<java.lang.Integer> get() {
		return seq;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		for (int i : seq) {
			_out.writeInt(i);
		}
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		readSequence(_in);
	}
	
	public void deserialize(long _id, Hash _hash, int _type, int _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		readSequence(_in);
	}

	private void readSequence(DataInputStream _in) throws IOException {
		long  size = super.size / 4;
		seq = new ArrayList<java.lang.Integer>();
		for(long l = 0 ; l < size ; ++l) {
			seq.add(_in.readInt());
		}
	}
	
	public String toString() {
		String buff = super.toString() + " [ ";
		for(int i : seq) {
			buff += i + " ";
		}
		buff += "]";
		return buff;
	}
}
