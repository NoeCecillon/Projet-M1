package aide;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Fen�tre d'aide contenant les informations sur le logiciel et l'aide.
 */
public class FenetreAide extends JFrame {

	// Les 3 onglets de la fen�tre d'aide
	private static OngletPresentation ongPresentation;
	private static OngletMembres ongMembres;
	private static OngletAide ongAide;
	// Le conteneur avec les 3 onglets
	private JTabbedPane tabbedPane;
	
	public static void main(String[] a) {
		FenetreAide f = new FenetreAide();
	}
	
	/**
	 * Constructeur de base. Cr�e la fen�tre d'aide.
	 */
	public FenetreAide() {
		initialiser();
	}
	
	/**
	 * Constructeur avec un argument. Cr�e la fen�tre d'aide et s�lectionne de base l'onglet dont le nom est donn� en argument.
	 * @param ongletOuvert Nom de l'onglet � s�lectionner � l'ouverture.
	 */
	public FenetreAide(String ongletOuvert) {
		initialiser();
		if (ongletOuvert.equals("Pr�sentation")) {
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
	 * Constructeur a 2 arguments. Cr�e la fen�tre d'aide et ouvre l'onglet aide en allant � l'ancre sp�cifi�e.
	 * Le premier argument doit �tre "Aide".
	 * @param ongletOuvert Nom de la fen�tre � ouvrir. Doit �tre "Aide" pour fonctionner correctement.
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
	 * Initialise une nouvelle fen�tre d'aide.
	 */
	public void initialiser() {
		setVisible(true);
		//Pour fermer cette fen�tre mais pas l'application enti�re
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 600, 600);
		//pour placer la fen�tre au milieu de l'�cran
		setLocationRelativeTo(null);
		
		//On cr�e une fen�tre avec 3 onglets 
		tabbedPane = new JTabbedPane();
		ongPresentation = new OngletPresentation();
		tabbedPane.addTab("Pr�sentation", ongPresentation);
		ongMembres = new OngletMembres();
		tabbedPane.addTab("Membres", ongMembres);
		ongAide = new OngletAide();
		tabbedPane.addTab("Aide", ongAide);
		add(tabbedPane);
		revalidate();
	}
	
}
