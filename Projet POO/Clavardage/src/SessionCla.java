import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

public class SessionCla {

	private InetAddress dest ;
	private String pseudoDest ;
	private DatagramSocket dgramSocket;
	
	public SessionCla (InetAddress destin, String p) {
		this.dest = destin ;
		this.pseudoDest = p ;
	}

	public void generateConnection(Message_Controle s) {
		ServerSocket s1;
		int port = 65535;
		try {
			s1 = new ServerSocket(0);
			port = s1.getLocalPort(); //allows us to find the next unoccupied port number
			s1.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			dgramSocket = new DatagramSocket(4500);
			String mes = s.stateToString(s.getState())+port;
			DatagramPacket outPacket= new DatagramPacket(mes.getBytes(), mes.length(),this.dest, 3000);
			dgramSocket.send(outPacket);
			dgramSocket.close();
			ServerTCP serv = new ServerTCP(this.dest, pseudoDest, true, port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
}