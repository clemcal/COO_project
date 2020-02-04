import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ClientTCPImage implements Runnable{
	private Thread t;
	static BufferedImage bimg;
	private String path;
	private InetAddress server;
	int port = 6066;

	public ClientTCPImage(InetAddress serverName, String pat){
		this.server = serverName;
		this.path = pat;
		t= new Thread(this);
		t.start();
	}

	public void run() {
		try
		{
			Socket client = new Socket(server, port);
			DataInputStream in=new DataInputStream(client.getInputStream());
			DataOutputStream out =
					new DataOutputStream(client.getOutputStream());
			java.util.Properties properties = System.getProperties();
			String home = properties.get("user.home").toString();
			String separator = properties.get("file.separator").toString();

			ImageIcon img1=new ImageIcon(home+separator+"test.jpg");
			Image img = img1.getImage();
			Image newimg = img.getScaledInstance(100, 120,  java.awt.Image.SCALE_SMOOTH);

			bimg = ImageIO.read(new File(path));

			ImageIO.write(bimg,"JPG",client.getOutputStream());
			client.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}