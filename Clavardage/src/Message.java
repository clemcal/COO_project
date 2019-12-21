import java.net.InetAddress;

public class Message {
	private InetAddress ipExpéditeur ;
	private long Heure ; //pas sur
	private boolean isData;
	
	public Message(InetAddress exp, long date, boolean isD) {
		this.ipExpéditeur = exp ;
		this.Heure = date ;
		this.isData = isD;
	}
	

	public boolean getisData() {return this.isData;}
	
}
