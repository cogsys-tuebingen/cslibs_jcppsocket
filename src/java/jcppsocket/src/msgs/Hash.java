package msgs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Hash {
	final int nBytes;
	byte[]    bytes;
	
	public Hash(int bits) {
		nBytes = bits / 8;
		bytes  = new byte[nBytes];
		Arrays.fill(bytes, (byte) 0);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		for(int i = 0 ; i < nBytes ; ++i) 
			bytes[i] = in.readByte();
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		for(int i = 0 ; i < nBytes ; ++i)
			out.writeByte(bytes[i]);	
	}
	
	public String toString() {
		String buff = "[";
		for(int i = 0 ; i < nBytes ; ++i)
			buff += " " + Byte.toString(bytes[i]);
		buff += " ]";
		return buff;
	}
}