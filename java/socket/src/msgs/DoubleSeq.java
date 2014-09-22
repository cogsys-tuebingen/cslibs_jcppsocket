package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DoubleSeq extends SocketMsg {
	private double[] seq = null; //new ArrayList<java.lang.Double>();

	public DoubleSeq() {
		super.size = 0;
		super.type = DataType.double_t;
	}

	public DoubleSeq(final double[] _seq) {
		seq = _seq.clone();
		super.size = 8 * seq.length;
		super.type = DataType.double_t;
	}

	public DoubleSeq(final double[] _seq, final long _id) {
		seq = _seq.clone();
		super.id = _id;
		super.size = 8 * seq.length;
		super.type = DataType.double_t;
	}
	
	public DoubleSeq(final double[] _seq, final long _id, final Hash _hash) {
		seq = _seq.clone();
		super.id = _id;
		super.hash = _hash;
		super.size = 8 * seq.length;
		super.type = DataType.double_t;
	}	

	public DoubleSeq( long _id, Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.double_t;
	}

	public void set(final double[]_seq) {
		seq 	   = _seq.clone();
		super.size = 8 * _seq.length;
	}

	public double[] get() {
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

	public void deserialize(long _id, Hash _hash, int _type, int _size, DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		readSequence(_in);
	}

	private void readSequence(DataInputStream _in)
			throws IOException {
		int seqLength = super.size / 8;
		seq = new double[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			seq[l] = _in.readDouble();
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
