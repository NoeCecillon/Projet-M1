package barreMenu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import affichageCentral.AfficheurFluxJulia;
import logs.FenetreConsole;
import main.FenetrePrincipale;

/**
 * Permet de lancer la g�n�ration avec Julia dans un thread s�par�.
 */
public class ThreadGeneration extends Thread {

	String noeuds;
	String densite;
	String angle;
	String nbCommodite;
	FenetrePrincipale parent;
	
	public ThreadGeneration(String noeuds, String densite, String angle, String nbCommodite, FenetrePrincipale parent) {
		this.noeuds = noeuds;
		this.densite = densite;
		this.angle = angle;
		this.nbCommodite = nbCommodite;
		this.parent = parent;
	}
	
    public void run(){
    	Runtime runtime = Runtime.getRuntime();
    	Process proc;
		try {
			//Lit le fichier de configuration pour r�cup�rer le chemin de Julia.
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			String ligne;
			String pathJulia = null;
			while ((ligne = br.readLine()) != null) {
				if (ligne.startsWith("Julia: ")) {
					//Cette variable contient le chemin vers Julia 
					pathJulia = ligne.replace("Julia: ", "");
				}
			}
			
			/* R�cup�re le chemin absolu vers le fichier generateur.jl */
			File methodeGeneration = new File("generateur"+System.getProperty("file.separator")+"generateur.jl");
			String pathMethodeGeneration = methodeGeneration.getAbsolutePath();
			
			// Execute une ligne de commande pour lancer la g�n�ration avec julia
			// 1er param = Chemin vers Julia
			// 2�me param = permet de ne pas afficher les warnings de julia
			// 3�me param = chemin vers le g�n�rateur
			// 4�me param = nombre de noeuds � g�n�rer
			// 5�me param = densit� du r�seau � g�n�rer
			// 6�me param = angle alpha pour le r�seau � g�n�rer
			// 7�me param = nombre de commodit�s � g�n�rer
			proc = runtime.exec(new String[] { pathJulia, "--depwarn=no" , pathMethodeGeneration, noeuds, densite, angle, nbCommodite});
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
			new Thread(fluxSortie).start();
			proc.waitFor();

			/* Affichage texte et affiche la progressbar */
			FenetreConsole.getFenetreConsole().printConsole("Generation termin�e");
			FenetreConsole.getFenetreConsole().printSimplex(fluxSortie.result);
			parent.finChargement();
			/*Une fois la g�n�ration termin�e on affiche l'instance */
			parent.previsualiserInstance("generateur"+ System.getProperty("file.separator") +"res", false);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }