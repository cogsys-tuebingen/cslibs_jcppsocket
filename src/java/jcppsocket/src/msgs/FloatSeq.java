package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatSeq extends SocketMsg {
	private float[] seq = new float[0];
		
	public FloatSeq() {
		super.size = 0;
		super.type = DataType.FLOAT_T;
	}
	
	public FloatSeq(final float[] _seq) {
		this.seq 		  = _seq.clone();
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final float[] _seq, 
					final long    _id) {
		this.seq           = _seq.clone();
		super.id      = _id;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final float[] _seq, 
					final long 	  _id, 
					final Hash    _hash) {
		this.seq           = _seq.clone();
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final long _id, 
					final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	void set(final float[]_seq) {
		this.seq 	   = _seq.clone();
		super.size = 4 * _seq.length;
	}
	
	float[] get() {
		return this.seq;
	}
	
	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		for (float f : this.seq) {
			_out.writeFloat(f);
		}
	}
	
	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		readSequence(_in);
	}

	public void deserialize(final long 		_id, 
							final Hash 		_hash, 
							final int  		_type,
							final int 		_dataOrg,
							final int  		_size, 
							DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _dataOrg, _size, _in);
		readSequence(_in);
	}
	
	private void readSequence(DataInputStream _in)
			throws IOException {
		int seqLength = super.size / 4;
		this.seq = new float[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			this.seq[l] = _in.readFloat();
		}
	}
	
	public String toString() {
		String buff = super.toString() + " [ ";
		for(float f : this.seq) {
			buff += f + " ";
		}
		buff += "]";
		return buff;
	}
}
