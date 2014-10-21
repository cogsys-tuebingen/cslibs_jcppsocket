package socket;

import java.io.IOException;

import msgs.CharSeq;
import msgs.SocketMsg;

class Test {
	public static void main(String [] args) {
		class StringProvider implements ServiceProvider {
			@Override
			public SocketMsg process(SocketMsg in) {
				System.out.println("I am the great Provider!");
				
				if(in instanceof CharSeq) {
					CharSeq seq = (CharSeq) in;
					String  str = new String(seq.get());
					System.out.println("GOT STRING '" + str + "'!");
				}
				
				return in;
			}
		}
		
		try {
			StringProvider strPr = new StringProvider();
			Server s = new Server(6666);
			s.registerProvider(strPr);
			s.start();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}