package msgs;

public class LoggedOn extends SocketMsg {
	
	public LoggedOn() {
		super.size    = 0;
		super.type    = DataType.LOGGED_ON_T;		
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public LoggedOn(final long _id, 
			        final Hash _hash) {
		super.id      = _id;
		super.hash    = _hash;
		super.size    = 0;
		super.type    = DataType.LOGGED_ON_T;		
		super.dataOrg = DataType.SINGLE_DO;
	}
	
	public String toString() {
		return super.toString() + " [ LOGGED_ON ]";
	}
}
