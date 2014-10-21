package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Char extends SocketMsg {
	private byte value = 0;

	public Char() {
		super.size    = 1;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Char(final byte value) {
		this.value	  = value;
		super.size    = 1;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Char(final byte value, 
				 final long id) {
		this.value         = value;
		super.id      = id;
		super.size    = 1;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Char(final byte value, 
				 final long id, 
				 final Hash hash) {
		this.value         = value;
		super.id      = id;
		super.hash    = hash;
		super.size    = 1;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Char(final long id,
			     final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 1;
		super.type    = DataType.CHAR_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	void set(final byte value) {
		this.value = value;
	}

	byte get() {
		return this.value;
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeByte(this.value);
	}

	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.value = in.readByte();
	}

	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type, 
							final int		dataOrg,
							final int  		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		this.value = in.readByte();
	}

	public String toString() {
		return super.toString() + " [ " + this.value + " ]";
	}
}
