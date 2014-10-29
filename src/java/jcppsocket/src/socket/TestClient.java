package socket;

import msgs.CharSeq;
import msgs.Error;
import msgs.SocketMsg;


class TestClient {
	public static void main(String [] args) {
		SyncClient sc = new SyncClient("127.0.0.1", 6666);
				
		String testStr 	= "I test if i decide to test!";
		CharSeq seq 	= new CharSeq();
		seq.set(testStr.getBytes());
		
		sc.connect();
		System.out.println("Connected ?" + sc.isConnected());
		
		SocketMsg resp = sc.query(seq);
		
		if(resp == null) {
			System.err.println("Socket response was null!");
		}
		
		if(resp instanceof Error) {
			Error err = (Error) resp;
			System.err.println("Got socket error!");
			System.err.println(err.toString());
		}
		
		if(resp instanceof CharSeq) {
			CharSeq respSeq = (CharSeq) resp;
			String str = new String(respSeq.get());
			System.out.println(str);
		}
		
		sc.disconnect();
		
		System.out.println("Tested and was good!");
	}
}