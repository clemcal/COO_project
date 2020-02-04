import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class History {
	public String fichier;
	private File file;
	private File dir;
	private String directoryName;
	private String path;
	private String psDest;


	public History(String fich, String pseudoDest) {
		this.psDest = pseudoDest;
		this.directoryName = "Historique";
		this.fichier = fich+".txt";
		java.util.Properties properties = System.getProperties();
		String home = properties.get("user.home").toString();
		String separator = properties.get("file.separator").toString();
		this.dir = new File(home+separator+this.directoryName);		
		this.dir.mkdir();
		this.file = new File(dir,this.fichier);
		this.path = home+separator+this.directoryName+separator+this.fichier;
		newFichier();
	}


	public void newFichier() {
		try {
			this.file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFich(String message, Boolean moi) {
		GregorianCalendar calendar = new GregorianCalendar();
		Date time  = calendar.getTime();
		try {
			FileWriter fw = new FileWriter(this.path,true);
			if (moi) {
				fw.write(time + "| Moi : " + message + "\n");
			}
			else {
				fw.write(time + "| "+this.psDest+" : " + message + "\n");
			}
			fw.close();
		}
		catch (IOException e) {
			System.err.println("IOException:" + e.getMessage());
		}
	}

	public String readFich(){
		String lignes = "";
		String Newligne=System.getProperty("line.separator");
		try {
			for (String ligne : Files.readAllLines(Paths.get(this.path))) {
				lignes += (ligne + Newligne );
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}   
		return lignes;
	}
}