package affichageCentral;
import java.io.File;
import java.io.IOException;

/**
 * Permet de lancer la r�solution avec Julia dans un thread s�par�.
 */
public class ThreadJulia extends Thread {

	//Chemin vers le dossier contenant les fichiers d'instance. 
	String path;
	//Objet instance correspondant � l'instance que l'on va r�soudre.
	Instance parent;
	//Chemin vers la m�thode de r�solution (normalement dans le dossier methods).
	String cheminMethode;
	
	/**
	 * Constructeur 
	 * @param path Chemin vers le dossier contenant les 3 ou 4 fichiers de l'instance.
	 * @param parent Objet instance � r�soudre.
	 * @param cheminMethode Chemin vers la m�thode de r�solution � utiliser.
	 */
	public ThreadJulia(String path, Instance parent, String cheminMethode) {
		this.path = path;
		this.parent = parent;
		// On r�cup�re le chemin complet vers le fichier de la m�thode
		File f = new File(cheminMethode);
		this.cheminMethode = f.getAbsolutePath();
	}
	
	/**
	 * M�thode qui lance l'�x�cution de Julia.
	 */
    public void run(){
    	Runtime runtime = Runtime.getRuntime();
		Process proc;
		try {
			//�x�cute une commande dans le terminal pour lancer la r�solution par Julia.
			//1er param = Chemin vers Julia
			//2�me param = permet de ne pas afficher les warnings
			//3�me param = Chemin vers la m�thode de r�solution
			//4�me param = Chemin vers le dossier contenant l'instance avec un "\" ou "/" � la fin.
			proc = runtime.exec(new String[] { "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\bin\\julia.exe", "--depwarn=no" , cheminMethode , path+System.getProperty("file.separator")} );
			/* R�cup�re ce qui est �crit dans la console de Julia */
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
            AfficheurFluxJulia fluxErreur = new AfficheurFluxJulia(proc.getErrorStream());

            Thread t = new Thread(fluxSortie);
            //lance le thread de r�solution
            t.start();
            new Thread(fluxErreur).start();

            /* On attend la fin de l'ex�cution du thread pour lire les r�sultats */
            proc.waitFor();
            //On envoie les r�sultats � la m�thode qui va faire le traitement.
            parent.lectureResultats(fluxSortie.result);
            
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }