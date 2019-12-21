import java.lang.Thread.State;
import java.net.InetAddress;
import java.util.*;

public class User {
	
	private String macAddress ;
	private InetAddress ipAddress ;
	private String pseudo ;
	private boolean actif ;
	
	private Server serv;
	private Client cli;
	private Broadcast broad;
	
	public User (String mac, InetAddress ip, String p, boolean act) {
		this.macAddress = mac;
		this.ipAddress = ip;
		this.pseudo = p;
		this.actif = act;
	
	}
	
	
	
	/*GETTERS*/
	
	public String getMac() {return macAddress;}
	public InetAddress getIp() {return ipAddress;}
	public String getPseudo() {return pseudo;}
	
	
	public boolean isActive() {return actif;}
	
	public long getDate() {return new java.util.Date().getTime();}
	
	/*SETTERS*/
	
	public void setIp(InetAddress ip) {this.ipAddress = ip;}
	public void setPseudo(String p) {this.pseudo = p;}
	
	/*Methods*/
	
	public void connect() {
		this.actif = true ; 
		Message_Controle c = new Message_Controle(this.getIp(),this.getDate(), Message_Controle.state.connected); //on prévient tous le monde qu'on est connecté
		this.broad = new Broadcast(c);
		this.serv = new Server();
		this.serv.pseudo= this.pseudo ; 
	}
	
	
	public void startSession (String pseud) {
		InetAddress exp = this.ipAddress ;
		InetAddress dest = this.serv.getactivePeopleList().get(pseud) ;
		SessionCla session = new SessionCla (exp, dest, "") ;
		this.cli = new Client(dest) ;
		//à traiter : l'historique à renvoyer ici
	}

	public boolean AskForPseudo(String pse) {
		for (String i : this.serv.getactivePeopleList().keySet() ) {
			if (pse.equals(i)) {
				System.out.printf("Pseudo already used, please choose another one");
				return true;
			}
		}
		this.pseudo = pse ;
		System.out.println("Pseudo ok");
		Message_Controle p = new Message_Controle(this.getIp(),this.getDate(), Message_Controle.state.pseudo,this.pseudo); //on prévient tout le monde qu'on est connecté
		this.broad = new Broadcast(p);
		
		return false;
	}
	
	
	
	public void disconnect() {
		this.actif = false ; 
		Message_Controle c = new Message_Controle(this.getIp(),this.getDate(), Message_Controle.state.disconnected); //on prévient tous le monde qu'on est connecté
		this.broad = new Broadcast(c);
		this.cli.close();
		this.serv.close();
	}


}
