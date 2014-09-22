package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DoubleBlock extends SocketMsg {
	private double[] block = new double[0]; //new ArrayList<java.lang.Double>();
	private int		 rows = 0;
	private int 	 cols = 0;

	public DoubleBlock() {
		super.size    = 0;
		super.type    = DataType.double_t;
		super.dataOrg = DataType.block_do;

	}

	public DoubleBlock(final long _id) {
		super.id      = _id;
		super.size    = 0;
		super.type    = DataType.double_t;
		super.dataOrg = DataType.block_do;
	}

	public DoubleBlock(final long _id, 
			final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.double_t;
		super.dataOrg = DataType.block_do;
	}

	public void set(final double[] _block, 
					final int	   _step) {
		block 	   = _block.clone();
		cols       = _step;
		rows	   = _block.length / _step;
		super.size = 8 * _block.length;
	}

	public double[] get() {
		return block;
	}
	
	public double[] row(final int i) {
		return Arrays.copyOfRange(block, i * cols, i * cols + cols);
	}
	
	public double get(final int y,
					  final int x)
	{
		return block[y * cols + x];
	}

	public void serialize(DataOutputStream _out) throws IOException {
		super.serialize(_out);
		_out.writeInt(rows);
		_out.writeInt(cols);
		for (double d : block) {
			_out.writeDouble(d);
		}
	}

	public void deserialize(DataInputStream _in) throws IOException {
		super.deserialize(_in);
		rows = _in.readInt();
		cols = _in.readInt();
		readSequence(_in);
	}

	public void deserialize(
			final long 		_id, 
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
		int seqLength = (super.size - 2 * 4) / 8;
		block = new double[seqLength];
		for(int l = 0 ; l < seqLength ; ++l) {
			block[l] = _in.readDouble();
		}
	}

	public String toString() {
		String buff = super.toString() + "\n" + "[";
		for(int i = 0 ; i < rows ; ++i) {
			buff += "[ ";
			for(int j = 0 ; j < cols ; ++j)
				buff += block[i * cols + j] + " ";
			buff += "]";
			if(i < rows - 1)
				buff += "\n";
			
		}
		buff += "]";
		return buff;
	}
}
