package socket;

import java.io.IOException;

class Test {
	public static void main(String [] args)
	{
		try
		{
			Thread s = new Server(6666);
			s.start();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}