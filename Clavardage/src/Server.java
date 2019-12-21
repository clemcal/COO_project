import java.net.*;
import java.util.*;
import java.io.IOException;



public class Server implements Runnable{

	protected HashMap<String, InetAddress> activePeopleList ;
	public final static int port = 3000; //a rentrer dans notre manuel d'utilisateur
	private Thread t ;
	protected String pseudo ;
	protected String pseudo_dest ;
	private boolean end = false ;
	
	private DatagramSocket dgramSocket;

	public Server() {
		this.activePeopleList = new HashMap<String, InetAddress>();
		t= new Thread(this);
		t.start();
	}
	
	public HashMap<String, InetAddress> getactivePeopleList() {
		return activePeopleList ;
	}



	public void run() {
		while(!end) {
			try {
				//Create a DatagramSocketobject   
				dgramSocket = new DatagramSocket(port);


				//Create a buffer for incoming datagrams
				byte[] buffer = new byte[256];


				//Create a DatagramPacketobject for the incoming datagram
				DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);

				//Accept an incoming datagram
				dgramSocket.receive(inPacket);


				//Accept the sender’s address and port from the packet
				InetAddress clientAddress = inPacket.getAddress();
				int clientPort= inPacket.getPort();

				//Retrieve the data from the buffer
				String message = new String(inPacket.getData(), 0, inPacket.getLength());

				//on traite si on reçoit un message en broadcast, on doit lui renvoyer qu'on est connecté
				if (message.startsWith("0")) { //donc message de controle
					if (message.contains("connected")) {

						//Create the response datagram
						//peut-être a changer pour creer un message controle et traiter nos messages en bytes
						String response = "0" + "pseudo" + this.pseudo;
						DatagramPacket outPacket= new DatagramPacket(response.getBytes(), response.length(),clientAddress, clientPort);

						//Send the response datagram 
						dgramSocket.send(outPacket) ;
					}
					else if (message.contains("disconnected")) {
						this.activePeopleList.remove(pseudo_dest);
					}
					else if (message.contains("pseudo")) {
						pseudo_dest = message.substring(7) ;
						this.activePeopleList.put(pseudo_dest, clientAddress);
					}
					else {

					}
				}
				else { //message starts with "1"
					System.out.println(pseudo_dest + " dit : " + message);
					//et sauvegarder dans l'historique
				}		
			}


			catch (SocketException e) {
				//System.out.println("SocketExceptionServ");
				e.printStackTrace();  
			} 
			catch (IOException e) {
				System.out.println("IOExceptionServ");
			}	
		}
	}
	
	public void close() {dgramSocket.close(); end=true;}
}

