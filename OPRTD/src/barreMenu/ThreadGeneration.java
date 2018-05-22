package barreMenu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import affichageCentral.AfficheurFluxJulia;
import logs.FenetreConsole;
import main.FenetrePrincipale;

/**
 * Permet de lancer la génération avec Julia dans un thread séparé.
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
			//Lit le fichier de configuration pour récupérer le chemin de Julia.
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			String ligne;
			String pathJulia = null;
			while ((ligne = br.readLine()) != null) {
				if (ligne.startsWith("Julia: ")) {
					//Cette variable contient le chemin vers Julia 
					pathJulia = ligne.replace("Julia: ", "");
				}
			}
			
			/* Récupère le chemin absolu vers le fichier generateur.jl */
			File methodeGeneration = new File("generateur"+System.getProperty("file.separator")+"generateur.jl");
			String pathMethodeGeneration = methodeGeneration.getAbsolutePath();
			
			// Execute une ligne de commande pour lancer la génération avec julia
			// 1er param = Chemin vers Julia
			// 2ème param = permet de ne pas afficher les warnings de julia
			// 3ème param = chemin vers le générateur
			// 4ème param = nombre de noeuds à générer
			// 5ème param = densité du réseau à générer
			// 6ème param = angle alpha pour le réseau à générer
			// 7ème param = nombre de commodités à générer
			proc = runtime.exec(new String[] { pathJulia, "--depwarn=no" , pathMethodeGeneration, noeuds, densite, angle, nbCommodite});
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
			new Thread(fluxSortie).start();
			proc.waitFor();

			/* Affichage texte et affiche la progressbar */
			FenetreConsole.getFenetreConsole().printConsole("Generation terminée");
			FenetreConsole.getFenetreConsole().printSimplex(fluxSortie.result);
			parent.finChargement();
			/*Une fois la génération terminée on affiche l'instance */
			parent.previsualiserInstance("generateur"+ System.getProperty("file.separator") +"res", false);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }