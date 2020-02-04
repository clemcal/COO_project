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
	private int port = 3000; 

	Broadcast(Message_Controle messCont) {
		t= new Thread(this);
		t.start();
		this.messC = messCont;	
	}
	
	private InetAddress generateIPBroadcast() {
		InetAddress broadcast = null ;
		try {
			Enumeration<NetworkInterface> interfaces;
			interfaces = NetworkInterface.getNetworkInterfaces();
			InterfaceAddress interfaceAddress = interfaces.nextElement().getInterfaceAddresses().get(1);
			broadcast = interfaceAddress.getBroadcast();
		}
		catch (SocketException e) {System.out.println("SocketException");} 
		return broadcast;
	}

	public void run() {
		try {
			DatagramSocket dgramSocket = new DatagramSocket();
			String message = this.messC.stateToString(this.messC.getState())+this.messC.getPseudo();
			InetAddress adresse = generateIPBroadcast();
			DatagramPacket outPacketControle = new DatagramPacket(message.getBytes(), message.length(),adresse, this.port);
			dgramSocket.send(outPacketControle);
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
