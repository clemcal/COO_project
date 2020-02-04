import java.net.InetAddress;

public class Message_Controle {

	private String pseud = null;

	public static enum state {
		connected, disconnect, pseudo, session;
	}

	private state controle ;

	public Message_Controle(state cont) {
		this.controle = cont ;
	}

	public Message_Controle(state cont, String pse) {
		this.controle = cont;
		this.pseud = pse;
	}

	public state getState() {return this.controle;}

	public String getPseudo() {if (this.pseud != null) {return this.pseud;}else{return "";}};

	public String stateToString (state cont) {
		switch(cont) {
		case connected:
			return "connected"; 
		case disconnect:
			return "disconnect";
		case pseudo:
			return "pseudo";
		case session:
			return "session" ;
		default:
			return "erreur";
		}
	}
}