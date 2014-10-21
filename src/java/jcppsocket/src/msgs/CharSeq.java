package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CharSeq extends SocketMsg {
	private byte[] seq = new byte[0];

	public CharSeq() {
		super.size    = 0;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public CharSeq(final byte[] seq) {
		this.seq 	  = seq.clone();
		super.size    = seq.length;
		super.type 	  = DataType.CHAR_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public CharSeq(final byte[] seq, 
				   final long 	id) {
		this.seq 	  = seq.clone();
		super.id 	  = id;
		super.size	  = seq.length;
		super.type 	  = DataType.CHAR_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}
	
	public CharSeq(final byte[] seq, 
				   final long 	id, 
				   final Hash 	hash) {
		this.seq 	  = seq.clone();
		super.id	  = id;
		super.hash    = hash;
		super.size    = seq.length;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}	

	public CharSeq(final long id, 
					 final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 0;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SEQUENCE_DO;
	}

	public void set(final byte[] seq) {
		this.seq   = seq.clone();
		super.size = seq.length;
	}

	public byte[] get() {
		return seq;
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		for (byte b : seq) {
			out.writeByte(b);
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
		int seqLength = super.size;
		seq = new byte[seqLength];
		for(int l = 0 ; l < seqLength ; ++l) {
			seq[l] = in.readByte();
		}
	}

	public String toString() {
		String buff = super.toString() + " [ ";
		for(byte c : this.seq) {
			buff += c + " ";
		}
		buff += "]";
		return buff;
	}
}
