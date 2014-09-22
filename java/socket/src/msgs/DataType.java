package msgs;


public class DataType {
	final static int error_t     = -2;
	final static int undef_t     = -1;
	final static int char_t      =  0;
	final static int uchar_t     =  1;
	final static int uint_t      =  2;
	final static int int_t       =  3;
	final static int float_t     =  4;
	final static int double_t    =  5;
	final static int hash256_t   =  6;
	
	final static int undef_do    = -1;
	final static int single_do   =  0;
	final static int sequence_do =  1;
	final static int block_do	 =  2;
	
	public static SocketMsg getMessage(int type) {
		switch (type) {
		case int_t:
			return new msgs.Int();
		case float_t:
			return new msgs.Float();
		case double_t:
			return new msgs.Double();
		case error_t:
			return new msgs.Error();
		default:
			return null;
		}
	}

	public static SocketMsg getSeqMessage(int type) {
		switch (type) {
		case int_t:
			return new msgs.IntSeq();
		case float_t:
			return new msgs.FloatSeq();
		case double_t:
			return new msgs.DoubleSeq();
		default:
			return null;
		}
	}	
	
	public static SocketMsg getBlockMessage(int type){
		switch (type) {
		case int_t:
			return new msgs.IntBlock();
		case float_t:
			return new msgs.FloatBlock();
		case double_t:
			return new msgs.DoubleBlock();
		default:
			return null;
		}
	}
}
