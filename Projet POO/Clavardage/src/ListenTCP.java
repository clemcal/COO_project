import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ListenTCP implements Runnable{
	private Socket socket;
	private History hist ;
	private InterfaceGraphique interfas;

	public ListenTCP(Socket sock, History histo,InterfaceGraphique interf) {
		this.interfas = interf;
		this.hist = histo;
		this.socket = sock;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(this.socket.getOutputStream());
			while(!(interfas.getisClosed())) {
				String mess2 = in.readLine();
				if(!(mess2==null)){
					if(mess2.contains("_")) {
						out.write("\n");
						out.flush();
						interfas.getFrame().dispose();
						interfas.setisClosed();
					}
					else {this.hist.writeFich(mess2,false);}
				}
			}
			in.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}