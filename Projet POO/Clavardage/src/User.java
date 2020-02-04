import java.net.InetAddress;

public class User {

	private String pseudo ;
	public Server serv ;
	private Broadcast broad;


	public void connect() {
		this.serv = new Server();
		Message_Controle c = new Message_Controle(Message_Controle.state.connected);
		this.broad = new Broadcast(c);
		Thread t2;
		try {
			t2= new Thread(new ServerTCPIm(6066));
			t2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void startSession (String pseud) {
		InetAddress dest = this.serv.getactivePeopleList().get(pseud) ;
		Message_Controle ses = new Message_Controle(Message_Controle.state.session); //on envoie un message de controle de session pour que le destinataire ouvre un serveur TCP
		SessionCla session = new SessionCla (dest, pseud) ;
		session.generateConnection(ses);
	}


	public boolean AskForPseudo(String pse) {
		for (String i : this.serv.getactivePeopleList().keySet() ) {
			if (pse.equals(i)) {
				return true;
			}
		}
		this.pseudo = pse ;
		Message_Controle p = new Message_Controle(Message_Controle.state.pseudo,this.pseudo); //on prévient tout le monde qu'on est connecté
		this.broad = new Broadcast(p);
		return false;
	}

	public void disconnect() {
		Message_Controle c = new Message_Controle(Message_Controle.state.disconnect); //on prévient tous le monde qu'on est déconnecté
		this.broad = new Broadcast(c);
		this.serv.close();
	}
}
