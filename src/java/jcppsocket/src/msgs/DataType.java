package msgs;



public class DataType {
	final static int LOGGED_ON_T = -4;
	final static int LOGOFF_T	 = -3;
	final static int ERROR_T     = -2;
	final static int UNDEF_T     = -1;
	final static int CHAR_T      =  0;
	final static int UCHAR_T     =  1;
	final static int UINT_T      =  2;
	final static int INT_T       =  3;
	final static int FLOAT_T     =  4;
	final static int DOUBLE_T    =  5;
	final static int HASH_T   =  6;
	
	final static int UNDEF_DO    = -1;
	final static int SINGLE_DO   =  0;
	
	final static int SEQUENCE_DO =  1;
	final static int BLOCK_DO	 =  2;
	
	private static boolean notYetAvailable(final int type) {
		boolean nYA = type == UCHAR_T || type == UINT_T;
		return nYA;
	}
	
	public static SocketMsg getMessage(int type) {
		switch (type) {
		case INT_T:
			return new msgs.Int();
		case FLOAT_T:
			return new msgs.Float();
		case DOUBLE_T:
			return new msgs.Double();
		case ERROR_T:
			return new msgs.Error();
		case CHAR_T:
			return new msgs.Char();
		case LOGOFF_T:
			return new msgs.LogOff();
		case LOGGED_ON_T:
			return new msgs.LoggedOn();
		default:
			if(notYetAvailable(type))
				return new msgs.Error("Not yet available data type!");
			
			return null;
		}
	}

	public static SocketMsg getSeqMessage(int type) {
		switch (type) {
		case INT_T:
			return new msgs.IntSeq();
		case FLOAT_T:
			return new msgs.FloatSeq();
		case DOUBLE_T:
			return new msgs.DoubleSeq();
		case CHAR_T:
			return new msgs.CharSeq();
		default:
			if(notYetAvailable(type))
				return new msgs.Error("Not yet available data type!");
			
			return null;
		}
	}	
	
	public static SocketMsg getBlockMessage(int type){
		switch (type) {
		case INT_T:
			return new msgs.IntBlock();
		case FLOAT_T:
			return new msgs.FloatBlock();
		case DOUBLE_T:
			return new msgs.DoubleBlock();
		case CHAR_T:
			return new msgs.CharBlock();
		default:
			if(notYetAvailable(type))
				return new msgs.Error("Not yet available data type!");
			
			return null;
		}
	}
}
