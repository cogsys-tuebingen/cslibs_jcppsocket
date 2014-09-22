package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntSeq extends SocketMsg {
	private int[] seq = new int[0];
		
	public IntSeq() {
		super.size    = 0;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.sequence_do;
	}
	
	public IntSeq(final int[] _seq) {
		seq           = _seq.clone();
		super.size    = 4 * seq.length;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.sequence_do;
	}
	
	public IntSeq(final int[] _seq, 
				  final long  _id) {
		seq = _seq.clone();
		super.id      = _id;
		super.size    = 4 * seq.length;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.sequence_do;
	}
	
	public IntSeq(final int[] _seq, 
				  final long  _id, 
				  final Hash  _hash) {
		seq 		  = _seq.clone();
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 4 * seq.length;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.sequence_do;
	}
	
	public IntSeq(final long _id, 
				  final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.sequence_do;
	}
	
	void set(final int[] _seq) {
		seq 	   = _seq.clone();
		super.size = 4 * _seq.length;
	}
	
	int[] get() {
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
	
	public void deserialize(final long 		_id, 
							final Hash 		_hash, 
							final int  		_type,
							final int		_dataOrg,
							final int 	 	_size, 
							DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _dataOrg, _size, _in);
		readSequence(_in);
	}

	private void readSequence(DataInputStream _in)
			throws IOException {
		int seqLength = super.size / 4;
		seq = new int[seqLength];  //new ArrayList<java.lang.Double>();
		for(int l = 0 ; l < seqLength ; ++l) {
			seq[l] = _in.readInt();
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
