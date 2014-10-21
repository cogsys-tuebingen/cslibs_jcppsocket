package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Float extends SocketMsg {
	private float value = 0.f;

	public Float() {
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Float(final float value) {
		this.value	  = value;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	public Float(final float value, 
				 final long id) {
		this.value         = value;
		super.id      = id;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Float(final float value, 
				 final long id, 
				 final Hash hash) {
		this.value         = value;
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;

	}

	public Float(final long id,
			     final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.FLOAT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}

	void set(final float value) {
		this.value = value;
	}

	float get() {
		return this.value;
	}

	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeFloat(this.value);
	}

	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.value = in.readFloat();
	}

	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type, 
							final int		dataOrg,
							final int  		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		this.value = in.readFloat();
	}

	public String toString() {
		return super.toString() + " [ " + this.value + " ]";
	}
}
