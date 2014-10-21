package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleSeq extends SocketMsg {
	private double[] seq = new double[0]; //new ArrayList<java.lang.Double>();

	public DoubleSeq() {
		super.size    = 0;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public DoubleSeq(final double[] seq) {
		this.seq 	  = seq.clone();
		super.size    = 8 * seq.length;
		super.type 	  = DataType.DOUBLE_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public DoubleSeq(final double[] seq, 
				     final long 	id) {
		this.seq 	  = seq.clone();
		super.id 	  = id;
		super.size	  = 8 * seq.length;
		super.type 	  = DataType.DOUBLE_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public DoubleSeq(final double[] seq, 
					 final long 	id, 
					 final Hash 	hash) {
		this.seq 	  = seq.clone();
		super.id	  = id;
		super.hash    = hash;
		super.size    = 8 * seq.length;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}	

	public DoubleSeq(final long id, 
					 final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 0;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public void set(final double[] seq) {
		this.seq   = seq.clone();
		super.size = 8 * seq.length;
	}

	public double[] get() {
		return seq;
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		for (double f : seq) {
			out.writeDouble(f);
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
							final int		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		readSequence(in);
	}

	private void readSequence(DataInputStream in)
			throws IOException {
		int seqLength = super.size / 8;
		seq = new double[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			seq[l] = in.readDouble();
		}
	}

	public String toString() {
		String buff = super.toString() + " [ ";
		for(double d : this.seq) {
			buff += d + " ";
		}
		buff += "]";
		return buff;
	}
}
