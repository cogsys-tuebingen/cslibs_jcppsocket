package msgs;


public class DataType {
	final static int error_t   = -2;
	final static int undef_t   = -1;
	final static int char_t    =  0;
	final static int uchar_t   =  1;
	final static int uint_t    =  2;
	final static int int_t     =  3;
	final static int float_t   =  4;
	final static int double_t  =  5;
	final static int hash256_t =  6;

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
	
	public static boolean isSequence(int type, long size) {
		switch(type) {
		case char_t:
		case uchar_t:
			return size > 1;
		case int_t:
		case uint_t:
		case float_t:
			return size > 4;
		case double_t:
			return size > 8;
		case hash256_t:
			return size > 32;
		default:
			return false;
		}

	}


}
