package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DoubleSeq extends SocketMsg {
	private ArrayList<java.lang.Double> seq = new ArrayList<java.lang.Double>();

	public DoubleSeq() {
		super.size = 0;
		super.type = DataType.double_t;
	}

	public DoubleSeq(ArrayList<java.lang.Double> _seq) {
		seq = new ArrayList<java.lang.Double>();
		super.size = 8 * seq.size();
		super.type = DataType.double_t;
	}

	public DoubleSeq(ArrayList<java.lang.Double> _seq, long _id) {
		seq = new ArrayList<java.lang.Double>();
		super.id = _id;
		super.size = 8 * seq.size();
		super.type = DataType.double_t;
	}

	public DoubleSeq(ArrayList<java.lang.Double> _seq, long _id, Hash _hash) {
		seq = new ArrayList<java.lang.Double>();
		super.id   = _id;
		super.hash = _hash;
		super.size = 8 * seq.size();
		super.type = DataType.double_t;
	}

	public DoubleSeq( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.double_t;
	}

	void set(ArrayList<java.lang.Double> _seq) {
		seq 	   = new ArrayList<java.lang.Double>();
		super.size = 8 * _seq.size();
	}

	ArrayList<java.lang.Double> get() {
		return seq;
	}

	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		for (double f : seq) {
			_out.writeDouble(f);
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

	private void readSequence(DataInputStream _in)
			throws IOException {
		long  seqLength = super.size / 8;
		seq = new ArrayList<java.lang.Double>();
		for(long l = 0 ; l < seqLength ; ++l) {
			seq.add(_in.readDouble());
		}
	}

	public String toString() {
		String buff = super.toString() + " [ ";
		for(double d : seq) {
			buff += d + " ";
		}
		buff += "]";
		return buff;
	}
}
