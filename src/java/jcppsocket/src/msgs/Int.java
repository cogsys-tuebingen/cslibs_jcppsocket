package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Int extends SocketMsg {
	private int value = 0;
		
	public Int() {
		super.size = 4;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Int(final int value) {
		this.value         = value;
		super.size    = 4;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Int(final int  value, 
			   final long id) {
		this.value         = value;
		super.id      = id;
		super.size    = 4;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Int(final int  value, 
			   final long id, 
			   final Hash hash) {
		this.value         = value;
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.INT_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Int(final long id, 
			   final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 4;
		super.type    = DataType.INT_T;		
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	void set(final int value) {
		this.value = value;
	}
	
	int get() {
		return this.value;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeInt(this.value);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		this.value = in.readInt();
	}
	
	public void deserialize(final long 		id, 
							final Hash 		hash, 
							final int  		type,
							final int 		dataOrg,
							final int  		size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		this.value = in.readInt();
		System.out.println(this.value);
	}
	
	public String toString() {
		return super.toString() + " [ " + this.value + " ]";
	}
}
