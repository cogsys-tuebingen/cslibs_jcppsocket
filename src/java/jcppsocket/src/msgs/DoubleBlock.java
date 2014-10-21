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
		super.size    = 8;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.BLOCK_DO;

	}

	public DoubleBlock(final long id) {
		super.id      = id;
		super.size    = 8;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.BLOCK_DO;
	}

	public DoubleBlock(final long id, 
					   final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 8;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.BLOCK_DO;
	}

	public int rows()
	{
		return this.rows;
	}
	
	public int cols()
	{
		return this.cols;
	}
	
	public void set(final double[] block, 
					final int	   step) {
		this.block = block.clone();
		this.cols  = step;
		this.rows  = block.length / step;
		super.size = 8 * block.length + 8;
	}
	
	public void set(final double[][] block) {
		this.cols       = block[0].length;
		this.rows	   = block.length;
		super.size = 8 * this.rows * this.cols + 8;
		this.block = new double[this.rows*this.cols];
		for(int i = 0 ; i < this.rows ; ++i) {
			for(int j = 0 ; j < this.cols ; ++j) {
				this.block[i * this.cols + j] = block[i][j];
			}
		}
	}	

	public double[] get() {
		return block;
	}
	
	public double[] row(final int i) {
		return Arrays.copyOfRange(block, i * this.cols, i * this.cols + this.cols);
	}
	
	public double get(final int y,
					  final int x)
	{
		return block[y * this.cols + x];
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeInt(this.rows);
		out.writeInt(this.cols);
		for (double d : block) {
			out.writeDouble(d);
		}
	}

	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.rows = in.readInt();
		this.cols = in.readInt();
		readSequence(in);
	}

	public void deserialize(
			final long 		id, 
			final Hash 		hash, 
			final int  		type,
			final int 		dataOrg,
			final int		size, 
			DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		this.rows = in.readInt();
		this.cols = in.readInt();
		readSequence(in);
	}

	private void readSequence(DataInputStream in)
			throws IOException {
		int seqLength = (super.size - 2 * 4) / 8;
		block = new double[seqLength];
		for(int l = 0 ; l < seqLength ; ++l) {
			block[l] = in.readDouble();
		}
	}

	public String toString() {
		String buff = super.toString() + "\n" + "[";
		for(int i = 0 ; i < this.rows ; ++i) {
			buff += "[ ";
			for(int j = 0 ; j < this.cols ; ++j)
				buff += this.block[i * this.cols + j] + " ";
			buff += "]";
			if(i < this.rows - 1)
				buff += "\n";
			
		}
		buff += "]";
		return buff;
	}
}
