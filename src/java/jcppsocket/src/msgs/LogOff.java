package msgs;


public class LogOff extends SocketMsg {
		
	public LogOff() {
		super.size    = 0;
		super.type    = DataType.LOGOFF_T;		
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public LogOff(final long _id, 
			      final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.LOGOFF_T;		
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public String toString() {
		return super.toString() + " [ LOGOFF ]";
	}
}
