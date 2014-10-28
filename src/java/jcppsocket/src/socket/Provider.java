package socket;

public abstract class Provider implements Runnable{
	protected Session session;
	
	public Provider(Session session) {
		this.session = session;
	}
}
