import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class MaFenetre implements Runnable {
	private JFrame frame;
	private JFrame frame2;
	private boolean isConnected = false ;
	private JTextField pseudoChoisi;
	private String pseudo ;
	private JLabel lblNouveau;
	private JTextField NouveauPseudo;
	private JTextArea GensConnectes ;
	private JLabel lblChoisissezUnPseudo;
	private JButton btnConnexion;
	private User user ;
	private JLabel lblChoisissezUnPseudo_1;
	private JTextField PseudoDest;
	private Thread t ;	


	public MaFenetre() {
		this.t = new Thread(this);
		t.start();
	}

	public void setisConnected(boolean b) {
		this.isConnected = b ;
	}

	public boolean getisConnected() {
		return this.isConnected ;
	}

	public void setPseudo(String p) {
		this.pseudo = p ;
	}

	public String getPseudo() {
		return this.pseudo ;
	}

	public String listConnected() {
		return this.user.serv.afficherList() ;
	}


	public void run() {
		frame2 = new JFrame();
		frame2.setBounds(100, 100, 450, 300);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame = new JFrame();
		frame.setBounds(200, 200, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GensConnectes = new JTextArea();
		frame2.getContentPane().add(GensConnectes);
		GensConnectes.setEnabled(false);
		GensConnectes.setColumns(10);
		GensConnectes.setText("Personnes connectées :");

		this.user = new User();

		lblChoisissezUnPseudo = new JLabel("Choix de pseudo");
		lblNouveau = new JLabel("Changement de pseudo");

		KeyAdapter l = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					boolean samePseudo = user.AskForPseudo(pseudoChoisi.getText());
					if (samePseudo) {
						lblChoisissezUnPseudo.setText("Pseudo déjà utilisé, choisissez un autre pseudo");
					}
					else {
						setPseudo(pseudoChoisi.getText());
						lblChoisissezUnPseudo.setText("Pseudo choisi : " + getPseudo());
						pseudoChoisi.setEnabled(false);
						NouveauPseudo.setEnabled(true);
						PseudoDest.setEnabled(true);
						pseudoChoisi.setText("");


					}
				}
			}
		} ;

		btnConnexion = new JButton("Connexion");
		btnConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getisConnected()) { //on se déconnecte
					btnConnexion.setText("Connexion");
					user.disconnect() ;
					setisConnected(false) ;
					setPseudo("") ;
					lblChoisissezUnPseudo.setText("Choix de pseudo");
					pseudoChoisi.setEnabled(false);
					NouveauPseudo.setEnabled(false);
					pseudoChoisi.removeKeyListener(l);
					frame.dispose();
					frame2.dispose();
				}
				else {  //on se connecte
					user.connect();
					btnConnexion.setText("Déconnexion");
					setisConnected(true) ;
					pseudoChoisi.setEnabled(true);
					pseudoChoisi.addKeyListener(l);
				}
			}
		});

		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		frame.getContentPane().add(btnConnexion);

		lblChoisissezUnPseudo.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblChoisissezUnPseudo);

		pseudoChoisi = new JTextField();
		frame.getContentPane().add(pseudoChoisi);
		pseudoChoisi.setEnabled(false);
		pseudoChoisi.setColumns(10);

		lblNouveau.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblNouveau);

		NouveauPseudo = new JTextField();
		NouveauPseudo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					user.serv.activePeopleList.remove(getPseudo());
					boolean samePseudo = user.AskForPseudo(NouveauPseudo.getText());
					if (samePseudo) {
						lblChoisissezUnPseudo.setText("Pseudo déjà utilisé, choisissez un autre pseudo");
					}
					else {
						setPseudo(NouveauPseudo.getText());
						lblChoisissezUnPseudo.setText("Pseudo choisi : " + getPseudo());
						GensConnectes.setText("");
						try {
							Thread.sleep(10);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						NouveauPseudo.setText("");
					}
				}
			}
		});

		NouveauPseudo.setEnabled(false);
		NouveauPseudo.setColumns(10);
		frame.getContentPane().add(NouveauPseudo);

		lblChoisissezUnPseudo_1 = new JLabel("Choix de destinataire");
		lblChoisissezUnPseudo_1.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblChoisissezUnPseudo_1);

		PseudoDest = new JTextField();
		PseudoDest.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) { 
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String destinataire = PseudoDest.getText();
					InetAddress dest = user.serv.getactivePeopleList().get(destinataire) ;
					if (dest==null) {
						lblChoisissezUnPseudo_1.setText("Pseudo incorrect, choisissez un autre pseudo ");
						PseudoDest.setText("");
					}
					else {
						lblChoisissezUnPseudo_1.setText("Choix de destinataire");
						user.startSession(destinataire);
						PseudoDest.setText("");
					}
				}
			}
		});
		frame.getContentPane().add(PseudoDest);
		PseudoDest.setColumns(10);
		PseudoDest.setEnabled(false);

		this.frame2.setVisible(true);
		this.frame.setVisible(true);

		while (true) {
			if (this.isConnected) {
				GensConnectes.setText("Personnes connectées : \n" + user.serv.afficherList());
			}
			else {
				GensConnectes.setText("Connectez-vous pour voir les personnes connectées");
			}
		}
	}
}
