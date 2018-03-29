package logs;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class FenetreConsole extends JTabbedPane {

	public Onglet simplex;
	public Onglet console;
	/* Contient l'objet this */
	private static FenetreConsole objet;
	
	public FenetreConsole() {

		simplex = new Onglet();
		addTab("Simplex", simplex);
		
		console = new Onglet();
		addTab("Console", console);
		
		objet = this;
	}
	
	public void printConsole(String txt) {
		this.console.print(txt);
	}
	
	public void printSimplex(String txt) {
		this.simplex.print(txt);
	}
	
	/* Fonction statique permettant à toutes les autres classes de récupérer l'objet this pour pouvoir afficher des infos */
	public static FenetreConsole getFenetreConsole() {
		return objet;
	}
}
