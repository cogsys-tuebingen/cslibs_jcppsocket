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
	
	public FloatSeq(final float[] seq) {
		this.seq 	  = seq.clone();
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final float[] seq, 
					final long    id) {
		this.seq      = seq.clone();
		super.id      = id;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final float[] seq, 
					final long 	  id, 
					final Hash    hash) {
		this.seq      = seq.clone();
		super.id      = id;
		super.hash    = hash;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public FloatSeq(final long id, 
					final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 0;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	void set(final float[]seq) {
		this.seq   = seq.clone();
		super.size = 4 * seq.length;
	}
	
	float[] get() {
		return this.seq;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		for (float f : this.seq) {
			out.writeFloat(f);
		}
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		readSequence(in);
	}

	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type,
							final int 		dataOrg,
							final int  		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		readSequence(in);
	}
	
	private void readSequence(DataInputStream in)
			throws IOException {
		int seqLength = super.size / 4;
		this.seq = new float[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			this.seq[l] = in.readFloat();
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
