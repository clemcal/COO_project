import java.util.*;
import java.net.InetAddress;  

public class AppClavardage {
	
	
	
	public static void main(String[] args) {
		try {
		
			InetAddress ia = InetAddress.getLocalHost(); 
			User user = new User ("blabla",ia, "", false);
			InetAddress ia2 = InetAddress.getByName("10.1.5.22") ;
			User user2 = new User ("blabla",ia2, "", false);
			
			user.connect();
			System.out.printf("Sélectionnez un pseudo svp") ;
			
			boolean same_pseudo = true;
			while(same_pseudo) {
				Scanner sc = new Scanner(System.in);
				String pseudo = sc.nextLine();
				same_pseudo = user.AskForPseudo(pseudo);
			}
			
			/*user2.connect();
			System.out.printf("Sélectionnez un pseudo2 svp") ;
			
			boolean same_pseudo2 = true;
			while(same_pseudo2) {
				Scanner sc2 = new Scanner(System.in);
				String pseudo2 = sc2.nextLine();
				same_pseudo2 = user2.AskForPseudo(pseudo2);
			}
			*/	
			
		}
		catch (Exception e) {
			System.out.println("coucou");
		      //e.printStackTrace();  
	    }
		}
	}
