package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CharBlock extends SocketMsg {
	private char[] block = new char[0];
	private int		 rows = 0;
	private int 	 cols = 0;

	public CharBlock() {
		super.size    = 8;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.BLOCK_DO;

	}

	public CharBlock(final long id) {
		super.id      = id;
		super.size    = 8;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.BLOCK_DO;
	}

	public CharBlock(final long id, 
					  final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 8;
		super.type    = DataType.FLOAT_T;
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
	
	public void set(final char[] block, 
					final int	  step) {
		this.block 	   = block.clone();
		this.cols       = step;
		this.rows	   = block.length / step;
		super.size = 4 * block.length + 8;
	}
	
	public void set(final char[][] block) {
		this.cols       = block[0].length;
		this.rows	   = block.length;
		super.size = 4 * this.rows * this.cols + 8;
		for(int i = 0 ; i < this.rows ; ++i) {
			for(int j = 0 ; j < this.cols ; ++j) {
				this.block[i * this.cols + j] = block[i][j];
			}
		}
	}	
	
	public char[] get() {
		return block;
	}
	
	public char[] row(final int i) {
		return Arrays.copyOfRange(block, i * this.cols, i * this.cols + this.cols);
	}
	
	
	public char get(final int y,
					 final int x)
	{
		return block[y * this.cols + x];
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeInt(this.rows);
		out.writeInt(this.cols);
		for (char f : block) {
			out.writeFloat(f);
		}
	}

	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.rows = in.readInt();
		this.cols = in.readInt();
		readSequence(in);
	}

	public void deserialize(final long 		id, 
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
		int seqLength = (super.size - 2 * 4) / 4;
		this.block = new char[seqLength];
		for(int l = 0 ; l < seqLength ; ++l) {
			this.block[l] = in.readChar();
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
