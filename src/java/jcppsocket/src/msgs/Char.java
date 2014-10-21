package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Char extends SocketMsg {
	private char value = 0;

	public Char() {
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Char(final char value) {
		this.value	  = value;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Char(final char value, 
				 final long id) {
		this.value         = value;
		super.id      = id;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Char(final char value, 
				 final long id, 
				 final Hash hash) {
		this.value         = value;
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Char(final long id,
			     final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	void set(final char value) {
		this.value = value;
	}

	char get() {
		return this.value;
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeFloat(this.value);
	}

	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.value = in.readChar();
	}

	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type, 
							final int		dataOrg,
							final int  		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		this.value = in.readChar();
	}

	public String toString() {
		return super.toString() + " [ " + this.value + " ]";
	}
}
