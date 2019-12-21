import java.net.InetAddress;

public class SessionCla {

	private InetAddress user1 ; //string c'est son adresse IP
	private InetAddress user2 ;
	private String history ;
	
	public SessionCla (InetAddress u1, InetAddress u2, String his) {
		this.user1 = u1 ;
		this.user2 = u2 ;
		this.history = his ;
	}
	
}