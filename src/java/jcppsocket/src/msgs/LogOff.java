package msgs;


public class LogOff extends SocketMsg {
		
	public LogOff() {
		super.size    = 0;
		super.type    = DataType.logoff_t;		
		super.dataOrg = DataType.single_do;
	}
	
	public LogOff(final long _id, 
			      final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.logoff_t;		
		super.dataOrg = DataType.single_do;
	}
	
	public String toString() {
		return super.toString() + " [ LOGOFF ]";
	}
}
