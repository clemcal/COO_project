import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Broadcast implements Runnable{

	private Thread t;
	private Message_Controle messC;
	private InetAddress ipBroadcast; 

	

	Broadcast(Message_Controle messCont) {
		t= new Thread(this);
		t.start();
		this.messC = messCont;	
	}
	
	private void generateIPBroadcast() {
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			InterfaceAddress interfaceAddress = interfaces.nextElement().getInterfaceAddresses().get(1);
			String s = interfaceAddress.toString();
			
			{
				InetAddress broadcast = interfaceAddress.getBroadcast();
				this.ipBroadcast = broadcast;
			}
		}catch (SocketException e) {System.out.println("SocketException");} 
	}

	public void run() {
		while(true) {
			try {
				//Create a DatagramSocketobject
				DatagramSocket dgramSocket = new DatagramSocket();
				int port = 3000; //port en dur ou demandé plus loin

				//Create the outgoing datagram
				String message = "0"+ this.messC.stateToString(this.messC.getState())+this.messC.getPseudo();
				generateIPBroadcast();
				InetAddress adresse = this.ipBroadcast;
				DatagramPacket outPacketControle = new DatagramPacket(message.getBytes(), message.length(),adresse, port);

				//Send the datagram message
				dgramSocket.send(outPacketControle);

				//Close the DatagramSocket
				dgramSocket.close();
			}
			catch (SocketException e) {
				System.out.println("SocketException");
			} 
			catch (UnknownHostException e) {
				System.out.println("UnknownHost");
			} 
			catch (IOException e) {
				System.out.println("IOException");
			}
		}
	}
	
	
	
}
