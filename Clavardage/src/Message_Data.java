import java.net.InetAddress;

public class Message_Data extends Message {
	
	public String data ;
	private InetAddress ipDestinataire;
	
	public Message_Data(InetAddress dest, InetAddress exp, long date, String da) {
		super(exp,date,true);
		this.ipDestinataire = dest;
		this.data = da ;
	}
	
}
