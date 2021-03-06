import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ServerTCPIm extends Thread {
    private ServerSocket serverSocket;
    Socket server;

    public ServerTCPIm(int port) throws IOException, SQLException, ClassNotFoundException, Exception
    {
        serverSocket = new ServerSocket(port);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                server = serverSocket.accept();
                BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                JFrame frame = new JFrame();
                frame.getContentPane().add(new JLabel(new ImageIcon(img)));
                frame.pack();
                frame.setVisible(true);  

            }
            catch(SocketTimeoutException st)
            {
                System.out.println("Socket timed out!");
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            catch(Exception ex)
            {
                System.out.println(ex);
            }
        }
    }
}