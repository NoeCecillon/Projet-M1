package aide;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Fenêtre d'aide contenant les informations sur le logiciel et l'aide.
 */
public class FenetreAide extends JFrame {

	// Les 3 onglets de la fenêtre d'aide
	private static OngletPresentation ongPresentation;
	private static OngletMembres ongMembres;
	private static OngletAide ongAide;
	// Le conteneur avec les 3 onglets
	private JTabbedPane tabbedPane;
	
	public static void main(String[] a) {
		FenetreAide f = new FenetreAide();
	}
	
	/**
	 * Constructeur de base. Crée la fenêtre d'aide.
	 */
	public FenetreAide() {
		initialiser();
	}
	
	/**
	 * Constructeur avec un argument. Crée la fenêtre d'aide et sélectionne de base l'onglet dont le nom est donné en argument.
	 * @param ongletOuvert Nom de l'onglet à sélectionner à l'ouverture.
	 */
	public FenetreAide(String ongletOuvert) {
		initialiser();
		if (ongletOuvert.equals("Présentation")) {
			tabbedPane.setSelectedIndex(0);
		}
		if (ongletOuvert.equals("Membres")) {
			tabbedPane.setSelectedIndex(1);
		}
		if (ongletOuvert.equals("Aide")) {
			tabbedPane.setSelectedIndex(2);
		}
	}
	
	/**
	 * Constructeur a 2 arguments. Crée la fenêtre d'aide et ouvre l'onglet aide en allant à l'ancre spécifiée.
	 * Le premier argument doit être "Aide".
	 * @param ongletOuvert Nom de la fenêtre à ouvrir. Doit être "Aide" pour fonctionner correctement.
	 * @param ancre Le nom de l'ancre que l'on veut atteindre.
	 */
	public FenetreAide(String ongletOuvert, String ancre) {
		initialiser();
		if (ongletOuvert.equals("Aide")) {
			tabbedPane.setSelectedIndex(2);
			ongAide.goTo(ancre);
		}
	}
	
	/**
	 * Initialise une nouvelle fenêtre d'aide.
	 */
	public void initialiser() {
		setVisible(true);
		//Pour fermer cette fenêtre mais pas l'application entière
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 600, 600);
		//pour placer la fenêtre au milieu de l'écran
		setLocationRelativeTo(null);
		
		//On crée une fenêtre avec 3 onglets 
		tabbedPane = new JTabbedPane();
		ongPresentation = new OngletPresentation();
		tabbedPane.addTab("Présentation", ongPresentation);
		ongMembres = new OngletMembres();
		tabbedPane.addTab("Membres", ongMembres);
		ongAide = new OngletAide();
		tabbedPane.addTab("Aide", ongAide);
		add(tabbedPane);
		revalidate();
	}
	
}
