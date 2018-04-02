import java.io.File;
import java.io.IOException;

public class ThreadJulia extends Thread {

	String path;
	Instance parent;
	String cheminMethode;
	
	public ThreadJulia(String path, Instance parent, String cheminMethode) {
		this.path = path;
		this.parent = parent;
		// On r�cup�re le chemin complet vers le fichier de la m�thode
		File f = new File(cheminMethode);
		this.cheminMethode = f.getAbsolutePath();
	}
	
    public void run(){
    	Runtime runtime = Runtime.getRuntime();
		Process proc;
		try {
			proc = runtime.exec(new String[] { "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\bin\\julia.exe", "--depwarn=no" , cheminMethode , path+System.getProperty("file.separator")} );
			System.out.println("Attente ... ");
			/* R�cup�re ce qui est �crit dans la console de Julia */
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
            AfficheurFluxJulia fluxErreur = new AfficheurFluxJulia(proc.getErrorStream());

            Thread t = new Thread(fluxSortie);
            t.start();
            new Thread(fluxErreur).start();

            /* On attend la fin de l'ex�cution du thread pour lire les r�sultats */
            proc.waitFor();
            parent.lectureResultats(fluxSortie.result);
            
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }