package barreMenu;
import java.io.IOException;

import affichageCentral.AfficheurFluxJulia;
import logs.FenetreConsole;
import main.FenetrePrincipale;

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
			proc = runtime.exec(new String[] { "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\bin\\julia.exe", "--depwarn=no" , "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\ProjetM1\\angle1.jl", noeuds, densite, angle, nbCommodite});
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
			new Thread(fluxSortie).start();
			proc.waitFor();

			/* Affichage texte et affiche la progressbar */
			FenetreConsole.getFenetreConsole().printConsole("Generation terminée");
			FenetreConsole.getFenetreConsole().printSimplex(fluxSortie.result);
			parent.finChargement();
			/*Une fois la génération terminée on affiche l'instance */
			parent.previsualiserInstance("C:"+ System.getProperty("file.separator") +"Users"+ System.getProperty("file.separator") +"Jean"+ System.getProperty("file.separator") +"AppData"+ System.getProperty("file.separator")+"Local"+ System.getProperty("file.separator")+"Julia-0.5.2"+ System.getProperty("file.separator")+"ProjetM1"+ System.getProperty("file.separator")+"generateur", false);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }