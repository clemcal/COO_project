import java.net.*;
import java.io.*;

public class ServerTCP implements Runnable {

	private int port; 
	private boolean isExp;
	private Thread t;
	private InetAddress IPdest;
	private Socket socket;
	private ServerSocket socketServeur;
	private String pseudoDest ;
	private InterfaceGraphique ig ;
	
	public ServerTCP(InetAddress d, String pDest, Boolean exp, int po) {
		this.port = po;
		this.pseudoDest = pDest ;
		this.ig = new InterfaceGraphique(this.pseudoDest, d);
		this.isExp = exp;
		this.IPdest = d;
		t= new Thread(this);
		t.start();
	}

	public void run() {
		try {
			if (this.isExp) {
				try {
					Thread.sleep(500); //Here we wait for the server on the other side to be created
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					InetAddress serveur = this.IPdest;
					socket = new Socket(serveur, this.port);
				} catch (Exception e) {
					e.printStackTrace();
				}
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				Thread t2= new Thread(new ListenTCP(socket,ig.getHistory(),ig));
				t2.start();
				while(!(ig.getisClosed())) {
					while(!(ig.getisMessage())) {
						System.out.flush();
					}

					String mess = ig.getMessage();
					out.write(mess + "\n");
					out.flush();
					ig.setisMessage(false) ;
					ig.getHistory().writeFich(mess, true);
				}
				out.write("_\n");
				out.flush();
			}
			else {
				try {
					socketServeur = new ServerSocket(this.port);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Socket socketClient = socketServeur.accept();
				PrintWriter out = new PrintWriter(socketClient.getOutputStream());
				Thread t3 = new Thread(new ListenTCP(socketClient,ig.getHistory(),ig));
				t3.start();
				while (!(ig.getisClosed())) {
					System.out.flush();
					while (!(ig.getisMessage())) {
						Thread.sleep(1000);
						System.out.flush();
					}
					String rep = ig.getMessage() ;
					out.write(rep + "\n");
					out.flush();
					ig.getHistory().writeFich(rep, true);
					ig.setisMessage(false) ;
				}
				out.write("_\n");
				out.flush();
				socketServeur.close();
			}
		}catch (Exception e) {e.printStackTrace();}	
	}
}