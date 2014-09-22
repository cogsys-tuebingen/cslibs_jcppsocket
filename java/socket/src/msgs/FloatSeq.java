package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatSeq extends SocketMsg {
	private float[] seq = new float[0];
		
	public FloatSeq() {
		super.size = 0;
		super.type = DataType.float_t;
	}
	
	public FloatSeq(final float[] _seq) {
		seq = _seq.clone();
		super.size = 4 * seq.length;
		super.type = DataType.float_t;
	}
	
	public FloatSeq(final float[] _seq, 
					final long    _id) {
		seq = _seq.clone();
		super.id = _id;
		super.size = 4 * seq.length;
		super.type = DataType.float_t;
	}
	
	public FloatSeq(final float[] _seq, 
					final long 	  _id, 
					final Hash    _hash) {
		seq = _seq.clone();
		super.id   = _id;
		super.hash = _hash;
		super.size = 4 * seq.length;
		super.type = DataType.float_t;
	}
	
	public FloatSeq(final long _id, 
					final Hash _hash) {
		super.id   = _id;
		super.hash = _hash;
		super.size = 0;
		super.type = DataType.float_t;
	}
	
	void set(final float[]_seq) {
		seq 	   = _seq.clone();
		super.size = 4 * _seq.length;
	}
	
	float[] get() {
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

	public void deserialize(final long _id, 
							final Hash _hash, 
							final int  _type, 
							final int  _size, 
							DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _size, _in);
		readSequence(_in);
	}
	
	private void readSequence(DataInputStream _in)
			throws IOException {
		int seqLength = super.size / 4;
		seq = new float[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			seq[l] = _in.readFloat();
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
