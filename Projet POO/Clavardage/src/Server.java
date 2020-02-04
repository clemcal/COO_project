import java.net.*;
import java.util.*;
import java.io.IOException;

public class Server implements Runnable{

	protected HashMap<String, InetAddress> activePeopleList ;
	public final static int port = 3000;
	private Thread t ;
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

	//	public void afficherPeopleList() {
	//		System.out.println("liste : \n");
	//		for (String i : this.getactivePeopleList().keySet() ) {
	//			System.out.println("pseudo = " + i + "  IP = " + activePeopleList.get(i));
	//		}
	//	} 
	//	To help us debug

	public String afficherList() {
		String liste = "" ;
		for (String i : this.getactivePeopleList().keySet() ) {
			liste = liste + ("pseudo = " + i + "  IP = " + activePeopleList.get(i) +"\n");
		}
		return liste ;
	}

	public InetAddress generateIP() {
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			InterfaceAddress interfaceAddress = interfaces.nextElement().getInterfaceAddresses().get(1);
			return interfaceAddress.getAddress() ;
		}catch (SocketException e) {System.out.println("SocketException");}
		return null;
	}



	public void run() {
		try {

			dgramSocket = new DatagramSocket(port);
			while(!end) { //we are listening for UDP messages as long as the socket is open
				byte[] buffer = new byte[256];
				DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
				dgramSocket.receive(inPacket);
				InetAddress clientAddress = inPacket.getAddress();
				String pseudo_dest = "" ;

				for (String i : this.getactivePeopleList().keySet() ) { //we retrieve the pseudo corresponding to our client
					if (activePeopleList.get(i).equals(clientAddress)) {
						pseudo_dest = i ;
					}
				}

				String message = new String(inPacket.getData(), 0, inPacket.getLength());

				//upon receiving a message, we treat it differently according to its state
				if (message.contains("connected")) { //it is the message of connection : we answer with our pseudo
					String myPseudo ="" ;
					for (String i : this.getactivePeopleList().keySet() ) {
						if (activePeopleList.get(i).equals(generateIP())) {
							myPseudo = i ;
						}
					}
					String response = "pseudo" + myPseudo;
					DatagramPacket outPacket= new DatagramPacket(response.getBytes(), response.length(),clientAddress, port);
					dgramSocket.send(outPacket);

				}
				else if (message.contains("disconnect")) { //message of disconnection : we remove it from activePeopleList
					this.activePeopleList.remove(pseudo_dest);
				}
				else if (message.contains("pseudo")) { //we receive a pseudo :  we add it to our activePeopleList
					pseudo_dest = message.substring(6) ;
					for (String i : this.getactivePeopleList().keySet() ) {
						if (activePeopleList.get(i).equals(clientAddress)) {
							this.activePeopleList.remove(i);
							break ;
						}
					}
					if (!(pseudo_dest.equals(""))) {
						this.activePeopleList.put(pseudo_dest, clientAddress);
					}
				}
				else if (message.contains("session")) { //we are asked to start a new session : we create a ServerTCP (false means we are the server)
					int port_com = Integer.parseInt(message.substring(7));
					ServerTCP s = new ServerTCP(clientAddress, pseudo_dest, false, port_com);
				}

			}		
			dgramSocket.close(); 
		}

		catch (SocketException e) {
			System.out.println("socketException");
		} 
		catch (IOException e) {
			System.out.println("IOExceptionServ");
		}	
	}

	public void close() {end=true;} //we close the socket
}