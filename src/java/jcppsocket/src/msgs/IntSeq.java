package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntSeq extends SocketMsg {
	private int[] seq = new int[0];
		
	public IntSeq() {
		super.size    = 0;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public IntSeq(final int[] seq) {
		this.seq           = seq.clone();
		super.size    = 4 * this.seq.length;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public IntSeq(final int[] seq, 
				  final long  id) {
		this.seq = seq.clone();
		super.id      = id;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public IntSeq(final int[] seq, 
				  final long  id, 
				  final Hash  hash) {
		this.seq      = seq.clone();
		super.id      = id;
		super.hash    = hash;
		super.size    = 4 * this.seq.length;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public IntSeq(final long id, 
				  final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 0;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	void set(final int[] seq) {
		this.seq 	   = seq.clone();
		super.size = 4 * seq.length;
	}
	
	int[] get() {
		return this.seq;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		for (int i : this.seq) {
			out.writeInt(i);
		}
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		readSequence(in);
	}
	
	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type,
							final int		dataOrg,
							final int 	 	size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		readSequence(in);
	}

	private void readSequence(DataInputStream in)
			throws IOException {
		int seqLength = super.size / 4;
		this.seq = new int[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			this.seq[l] = in.readInt();
		}
	}
	
	
	public String toString() {
		String buff = super.toString() + " [ ";
		for(int i : this.seq) {
			buff += i + " ";
		}
		buff += "]";
		return buff;
	}
}
