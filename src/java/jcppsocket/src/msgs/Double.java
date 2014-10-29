package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Double extends SocketMsg {
	private double value = 0.0;
		
	public Double() {
		super.size    = 8;
		super.type 	  = DataType.DOUBLE_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Double(final double value) {
		this.value 	  = value;
		super.size 	  = 8;
		super.type 	  = DataType.DOUBLE_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Double(final double value, 
				  final long id) {
		this.value    = value;
		super.id      = id;
		super.size    = 8;
		super.type 	  = DataType.DOUBLE_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public Double(final double value, 
				  final long   id, 
				  final Hash   hash) {
		this.value    = value;
		super.id      = id;
		super.hash    = hash;
		super.size    = 8;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.SINGLE_DO;

	}
	
	public Double(final long id, 
				  final Hash hash) {
		super.id      = id;
		super.hash    = hash;
		super.size    = 8;
		super.type    = DataType.DOUBLE_T;
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public void set(final double value) {
		this.value = value;
	}
	
	public double get() {
		return value;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeDouble(value);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		value = in.readDouble();
	}
	
	public void deserialize(final long 	    id, 
							final Hash 		hash, 
							final int       type,
							final int 		dataOrg,
							final int 	    size, 
							DataInputStream in) throws IOException {
		super.deserialize(id, hash, type, dataOrg, size, in);
		value = in.readDouble();
	}
	
	public String toString() {
		return super.toString() + " [ " + this.value + " ]";
	}
	
}
