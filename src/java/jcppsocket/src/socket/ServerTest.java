package socket;

import java.io.EOFException;
import java.io.IOException;

import msgs.CharSeq;
import msgs.LogOff;
import msgs.SocketMsg;

class ServerTest {
	public static void main(String [] args) {
        ProviderFactory strPrFac =  new ProviderFactory() {
            class StringProvider extends Provider {

                public StringProvider(Session session) {
                    super(session);
                }

                public void run() {
                    System.out.println("I am the great Provider!");

                    while(true) {
                        SocketMsg in = null;
                        try {
                            in = session.read();
                        } catch (IOException e) {
                            e.printStackTrace();
                            continue;
                        } catch (Exception e) {
                        	e.printStackTrace();
                        	return;
						}

                        System.out.println(in.toString());

                        if(in instanceof LogOff) {
                            return;
                        }

                        try {
                            session.write(in);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                        	e.printStackTrace();
                        	return;
						}
                    }
                }
            }

            public Provider getInstance(Session session) {
                return new StringProvider(session);
            }
        };
		
		try {
			SyncServer s = new SyncServer(6666, 2);
			s.setProviderFactory(strPrFac);
            s.start();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}