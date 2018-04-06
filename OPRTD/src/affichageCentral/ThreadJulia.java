package affichageCentral;
import java.io.File;
import java.io.IOException;

/**
 * Permet de lancer la résolution avec Julia dans un thread séparé.
 */
public class ThreadJulia extends Thread {

	//Chemin vers le dossier contenant les fichiers d'instance. 
	String path;
	//Objet instance correspondant à l'instance que l'on va résoudre.
	Instance parent;
	//Chemin vers la méthode de résolution (normalement dans le dossier methods).
	String cheminMethode;
	
	/**
	 * Constructeur 
	 * @param path Chemin vers le dossier contenant les 3 ou 4 fichiers de l'instance.
	 * @param parent Objet instance à résoudre.
	 * @param cheminMethode Chemin vers la méthode de résolution à utiliser.
	 */
	public ThreadJulia(String path, Instance parent, String cheminMethode) {
		this.path = path;
		this.parent = parent;
		// On récupère le chemin complet vers le fichier de la méthode
		File f = new File(cheminMethode);
		this.cheminMethode = f.getAbsolutePath();
	}
	
	/**
	 * Méthode qui lance l'éxécution de Julia.
	 */
    public void run(){
    	Runtime runtime = Runtime.getRuntime();
		Process proc;
		try {
			//éxécute une commande dans le terminal pour lancer la résolution par Julia.
			//1er param = Chemin vers Julia
			//2ème param = permet de ne pas afficher les warnings
			//3ème param = Chemin vers la méthode de résolution
			//4ème param = Chemin vers le dossier contenant l'instance avec un "\" ou "/" à la fin.
			proc = runtime.exec(new String[] { "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\bin\\julia.exe", "--depwarn=no" , cheminMethode , path+System.getProperty("file.separator")} );
			/* Récupère ce qui est écrit dans la console de Julia */
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
            AfficheurFluxJulia fluxErreur = new AfficheurFluxJulia(proc.getErrorStream());

            Thread t = new Thread(fluxSortie);
            //lance le thread de résolution
            t.start();
            new Thread(fluxErreur).start();

            /* On attend la fin de l'exécution du thread pour lire les résultats */
            proc.waitFor();
            //On envoie les résultats à la méthode qui va faire le traitement.
            parent.lectureResultats(fluxSortie.result);
            
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }