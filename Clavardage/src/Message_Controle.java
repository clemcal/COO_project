import java.net.InetAddress;

public class Message_Controle extends Message {
	
	private String pseud = null;

	public enum state {
		connected, disconnected, pseudo ;
	}

	private state controle ;
	
	public Message_Controle(InetAddress exp, long date, state cont) {
		super(exp,date,false);
		this.controle=cont;
	}
	
	public Message_Controle(InetAddress exp, long date, state cont, String pse) {
		super(exp,date,false);
		this.controle = cont;
		this.pseud = pse;
	}
	
	public state getState() {return this.controle;}
	
	public String getPseudo() {if (this.pseud != null) {return this.pseud;}else{return "";}};
	
	public String stateToString (state cont) {
		switch(cont) {
		case connected:
			return "connected"; //message envoyé par le broadcast
		case disconnected:
			return "disconnected";
		case pseudo:
			return "pseudo";
		default:
			return "erreur";
			
			
		}
		
	}
}
