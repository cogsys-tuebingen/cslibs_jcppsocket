package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IntBlock extends SocketMsg {
	private int[] block = new int[0];
	private int	   rows = 0;
	private int    cols = 0;

	public IntBlock() {
		super.size    = 8;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.block_do;

	}

	public IntBlock(final long _id) {
		super.id      = _id;
		super.size    = 8;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.block_do;
	}

	public IntBlock(final long _id, 
			final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 8;
		super.type    = DataType.int_t;
		super.dataOrg = DataType.block_do;
	}

	public int rows()
	{
		return rows;
	}
	
	public int cols()
	{
		return cols;
	}
	
	public void set(final int[] _block, 
					final int   _step) {
		block 	   = _block.clone();
		cols       = _step;
		rows	   = _block.length / _step;
		super.size = 4 * _block.length + 8;
	}
	
	public void set(final int[][] _block) {
		cols       = _block[0].length;
		rows	   = _block.length;
		super.size = 4 * rows * cols + 8;
		for(int i = 0 ; i < rows ; ++i) {
			for(int j = 0 ; j < cols ; ++j) {
				block[i * cols + j] = _block[i][j];
			}
		}
	}	

	public int[] get() {
		return block;
	}
	
	public int[] row(final int i) {
		return Arrays.copyOfRange(block, i * cols, i * cols + cols);
	}
	
	public float get(final int y,
					 final int x)
	{
		return block[y * cols + x];
	}

	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeInt(rows);
		_out.writeInt(cols);
		for (int i : block) {
			_out.writeInt(i);
		}
	}

	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		rows = _in.readInt();
		cols = _in.readInt();
		readSequence(_in);
	}

	public void deserialize(final long 		_id, 
							final Hash 		_hash, 
							final int  		_type,
							final int 		_dataOrg,
							final int		_size, 
							DataInputStream _in) throws IOException {
		super.deserialize(_id, _hash, _type, _dataOrg, _size, _in);
		rows = _in.readInt();
		cols = _in.readInt();
		readSequence(_in);
	}

	private void readSequence(DataInputStream _in)
			throws IOException {
		int seqLength = (super.size - 2 * 4) / 4;
		block = new int[seqLength];
		for(int l = 0 ; l < seqLength ; ++l) {
			block[l] = _in.readInt();
		}
	}

	public String toString() {
		String buff = super.toString() + "\n" + "[";
		for(int i = 0 ; i < rows ; ++i) {
			buff += "[ ";
			for(int j = 0 ; j < cols ; ++j)
				buff += block[i * cols + j] + " ";
			buff += "]";
		}
		buff += "]";
		return buff;
	}
}
