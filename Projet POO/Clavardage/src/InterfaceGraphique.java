import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class InterfaceGraphique implements Runnable {


	private JFrame frame;
	private String destinataire;
	private boolean isMessage ;
	private boolean isClosed = false;
	private String message = "" ;
	private JTextArea Histo;
	private JButton btnEnvoyer;
	private JButton btnQuitter;
	private String recMes ;
	protected History histo ;
	private Thread t ;
	private InetAddress Ip;
	public String messageEnv;
	public String pathImage ;

	/**
	 * @wbp.parser.entryPoint
	 */
	public InterfaceGraphique(String dest, InetAddress IP) {
		this.Ip = IP;
		this.destinataire = dest ;
		int IPint = ipToInt(IP) ; //on transforme l'adresse IP en int afin de le mettre dans le nom du fichier de l'historique
		this.histo = new History("history" + IPint , this.destinataire) ;
		this.t = new Thread(this);
		t.start();
		//initialize() ;
	}

	public static int ipToInt(InetAddress ipAddr)
	{
		int compacted = 0;
		byte[] bytes = ipAddr.getAddress();
		for (int i=0 ; i<bytes.length ; i++) {
			compacted += (bytes[i] * Math.pow(256,4-i-1));
		}
		return compacted;
	}
	
	public JFrame getFrame() {return this.frame;}

	public InetAddress getIPdest() {return this.Ip;}

	public History getHistory() {
		return this.histo;
	}

	public boolean getisClosed() {
		return this.isClosed ;
	}

	public void setisClosed() {
		this.isClosed = true ;
	}

	public void newMessage(String mess) {
		this.isMessage = true ;
		this.message = mess ;
	}

	public void setisMessage(boolean is) {
		this.isMessage = is ;
	}

	public String getMessage() {
		return this.message ;
	}

	public boolean getisMessage() {
		return this.isMessage ;
	}

	public String getRecMes() {
		return this.recMes ;
	}


	public void run() {
		this.isMessage = false ;

		frame = new JFrame();
		frame.getContentPane().setFont(new Font("EB Garamond 08", Font.PLAIN, 12));
		frame.getContentPane().setForeground(Color.PINK);
		frame.setBounds(100, 100, 450, 307);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblDestinataire = new JLabel("Destinataire : " + this.destinataire);
		lblDestinataire.setVerticalAlignment(SwingConstants.TOP);
		lblDestinataire.setHorizontalAlignment(SwingConstants.LEFT);
		lblDestinataire.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDestinataire.setForeground(Color.ORANGE);
		frame.getContentPane().add(lblDestinataire);

		JLabel lblHistorique = new JLabel("Historique");
		frame.getContentPane().add(lblHistorique);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane_1);

		Histo = new JTextArea();
		scrollPane_1.setViewportView(Histo);
		Histo.setColumns(10);
		Histo.setText(histo.readFich()) ;





		JLabel lblEcrivezVotreMessage = new JLabel("Ecrivez votre message ici");
		frame.getContentPane().add(lblEcrivezVotreMessage);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane_3);

		JEditorPane ZoneTexte = new JEditorPane();
		scrollPane_3.setViewportView(ZoneTexte);

		ZoneTexte.setToolTipText("");
		ZoneTexte.setForeground(new Color(51, 51, 51));

		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageEnvoyé = ZoneTexte.getText();
				newMessage(messageEnvoyé);
				ZoneTexte.setText("");
			}
		});
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		frame.getContentPane().add(btnEnvoyer);

		btnQuitter = new JButton("Quitter la conversation");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				setisClosed() ;
				frame.dispose();
			}
		});
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnNewButton = new JButton("Envoyer une image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					pathImage = selectedFile.getAbsolutePath();
					ClientTCPImage serv = new ClientTCPImage(getIPdest(),pathImage);
				}
			}
		});
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnQuitter);

		this.frame.setVisible(true);

		while (true) {
			messageEnv = ZoneTexte.getText();
			Histo.setText(histo.readFich());
		}
	}
}