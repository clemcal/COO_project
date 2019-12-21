import java.net.*;
import java.util.Scanner;
import java.io.IOException;

public class Client implements Runnable{

	private Thread t;
	private DatagramSocket dgramSocket;
	private InetAddress ipadd ; 
	private boolean end = false ;

	
	Client(InetAddress ip) {
		this.ipadd = ip ;
		t= new Thread(this);
		t.start();
	}

	public void run() {
		while(!end) {
			try {

				

				//Create a DatagramSocketobject
				dgramSocket = new DatagramSocket();
				InetAddress adresse = ipadd; 
				int port = 3000; //port en dur ou demandé plus loin


				//Create the outgoing datagram
				Scanner sc = new Scanner(System.in);
				String message = sc.nextLine();


				DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(),adresse, port);

				//Send the datagram message
				dgramSocket.send(outPacket);

				/*//Create a buffer for incoming datagrams
				byte[] buffer = new byte[256];

				//Create a DatagramPacketobject for the incoming datagram
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);

				//Accept an incoming datagram
				dgramSocket.receive(inPacket);

				//Retrieve the data from the buffer

				String response = new String(inPacket.getData(), 0, inPacket.getLength());
				System.out.println("reponse = " + response);


				String message2 = "fin";
				DatagramPacket outPacket2 = new DatagramPacket(message2.getBytes(), message2.length(),adresse, port);
				dgramSocket.send(outPacket2);		*/


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
	
	public void close() {dgramSocket.close();end=true;}

}	

