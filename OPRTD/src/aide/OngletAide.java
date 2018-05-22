package aide;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Onglet "Aide" de la fen�tre d'aide. Contient toute l'aide pour utiliser l'application.
 */
public class OngletAide extends JSplitPane {

	//Contient le texte d'aide
	private JTextPane contenuDroite;
	
	public OngletAide() {
		//La fen�tre est s�par�e en 2 parties : � droite le contenu et � gauche le sommaire
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		//LE SOMMAIRE
		//contenu dans un scrollpane
		JScrollPane conteneurG = new JScrollPane();
		JTextPane contenuGauche = new JTextPane();
		conteneurG.setLayout(new ScrollPaneLayout());
		contenuGauche.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contenuGauche.setContentType("text/html");
		contenuGauche.setBounds(0, 0, 10000, 10000);
		//Renvoie vers le haut de la page
		contenuGauche.scrollToReference("top");
		conteneurG.setViewportView(contenuGauche);
		//Le texte � afficher. Mis en forme avec du HTML
		contenuGauche.setText(
				"<a href=\"selectionExecutable\">S�lection de l'ex�cutable Julia</a><br>"
				+ "<a href=\"ajoutMethode\">Ajout d'une m�thode de r�solution</a><br>"
				+ "<a href=\"generationInstance\">G�n�ration d'une instance al�atoire</a><br>"
				+ "<a href=\"sauvegardeInstance\">Sauvegarde d'une instance</a><br>"
				+ "<a href=\"ouvertureInstance\">Ouverture d'une instance</a><br>"
				+ "<a href=\"resolutionInstance\">R�solution d'une instance</a><br>"
				+ "<a href=\"utilisationGenerale\">Notice g�n�rale</a><br>"
				+ "<a href=\"menuInformations\">Menu informations</a><br>"
				);
		
		//Gestion des clics sur les liens
		contenuGauche.addHyperlinkListener(new HyperlinkListener()
		{
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    		//Quand on clique sur un lien, nous renvoie vers l'ancre associ�e dans la fen�tre de droite
			           contenuDroite.scrollToReference((r.getDescription()));
		         }
		    }
		 });
		
		//LE CONTENU
		JScrollPane conteneurD = new JScrollPane();
		contenuDroite = new JTextPane();
		conteneurD.setLayout(new ScrollPaneLayout());
		contenuDroite.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contenuDroite.setContentType("text/html");
		contenuDroite.setBounds(0, 0, 10000, 10000);
		conteneurD.setViewportView(contenuDroite);
		//Le texte � afficher. Mis en forme avec du HTML
		// L'attribut 'id' en HTML est remplac� par l'attribut 'name'
		contenuDroite.setText(
				"<a name=\"top\"/>"
				+ "<a name=\"selectionExecutable\"/> <h1>S�lection de l'ex�cutable Julia</h1>"
				+ "Lors du premier lancement de l'application ou si le fichier <i>config.txt</i> a �t� supprim�, il sera n�cessaire d'indiquer l'emplacement de l'ex�cutable Julia que l'application doit utiliser."
				+ "Il s'agit du fichier <i>julia.exe</i> se trouvant la plupart du temps dans <i>[Dossier d'installation de Julia]/bin/julia.exe</i>.<br>"
				+ "Le fichier choisi est sauvegard� et il n'y aura donc plus besoin de faire cette manipulation lors des utilisations suivantes.<br><br>"
				+ "En cas d'erreur lors de la s�lection de ce fichier, supprimez le fichier <i>config.txt</i> dans le dossier d'installation d'OPRTD et relancez l'application pour pouvoir s�lectionner le fichier � nouveau."
				+ "<br><br><br>"
				+ "<a name=\"ajoutMethode\"/> <h1>Ajout d'une m�thode de r�solution</h1>"
				+ "Il est possible d'ajouter de nouvelles m�thodes de r�solution. Les m�thodes ajout�es doivent suivre les conditions suivantes pour avoir un fonctionnement correct de l'application : <br>"
				+ "<ul>"
				+ "<li>En d�but de fichier, juste apr�s les <code style=\"background-color:#d9d9db;\">using XXX</code>, il faut ajouter la ligne suivante <code style=\"background-color:#d9d9db;\">File=string(ARGS[1])</code>.</li>"
				+ "<li>Les chemins vers les fichiers <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i> doivent �tre remplac�s par <code style=\"background-color:#d9d9db;\">File*\"fichier_3.csv\"</code>.</li>"
				+ "<li>Le sous r�seau s�lectionn� doit �tre affich� en commen�ant par <code style=\"background-color:#d9d9db;\">subgraph :</code> puis avec tous les arcs sous la forme  <code style=\"background-color:#d9d9db;\">(27,20)   (57,27)   (58,57)   (64,50) ...</code></li>"
				+ "<li>Le r�sultat des commodit�s doit �tre affich� en commen�ant par <code style=\"background-color:#d9d9db;\">commodity X :</code> ou X est le num�ro de la commodit� et suivi des sommets utilis�s par cette commodit� sous la forme <code style=\"background-color:#d9d9db;\">commodity 1 :    (77,79)   (79,111)   (80,77) ...</code></li>"
				+ "<li>La ligne suivante doit contenir la longueur de la commodit� sous la forme <code style=\"background-color:#d9d9db;\">length : 3203.966569</code></li>"
				+ "</ul>"
				+ "Les m�thodes ajout�es sont sauvegard�es et pourront �tre utilis�es lors des prochains lancements du logiciel. Le nom des m�thodes qui sera utilis� dans le logiciel est le nom du fichier. Une fois qu'une m�thode a �t� ajout�e, le fichier .jl correspondant est copi� dans le dossier <i>methods</i> contenu dans le r�pertoire d'installation d'OPRTD. Il est possible de modifier directement ce fichier ou de le supprimer pour ne plus le voir dans l'application."
				+ "<br><br><br>"
				+ "<a name=\"generationInstance\"/> <h1>G�n�ration d'une instance al�atoire</h1>"
				+ "Il est possible de g�n�rer une instance al�atoire en choisissant un certain nombre de param�tres. Ces param�tres sont : <br>"
				+ "<ul>"
				+ "<li>Nombre de noeuds : un entier repr�sentant le nombre de noeuds � cr�er dans l'instance g�n�r�e. Il est conseill� d'en mettre au moins 8.</li>"
				+ "<li>Densit� : la densit� du r�seau qui doit �tre g�n�r�. Il s'agit d'un nombre compris entre 0 et 1. Il est conseill� de ne pas utiliser de valeur trop proche de 0 ou de 1.</li>"
				+ "<li>Angle alpha</li>"
				+ "<li>Nombre de commodit�s : un entier repr�sentant le nombre de commodit�s � cr�er. Il est conseill� d'en mettre au moins 8.</li>"
				+ "</ul>"
				+ "Lorsqu'une instance est g�n�r�e, 3 fichiers sont g�n�r�s dans le dossier <i>generateur/res</i> du r�pertoire d'installation du logiciel. "
				+ "A chaque nouvelle g�n�ration, ces fichiers sont r��crits. Pour ne pas perdre une instance g�n�r�e, il est possible de la <a href=\"sauvegardeInstance\">sauvegarder</a>. <br>"
				+ "Le fichier du g�n�rateur est accessible dans le dossier <i>generateur</i> mais il ne doit jamais �tre supprim�.<br><br>"
				+ "Il peut arriver que la g�n�ration n'aboutisse jamais notamment � cause de valeurs trop petites ou trop grandes. Dans ce cas, fermez l'application et relancez la."				
				+ "<br><br><br>"
				+ "<a name=\"sauvegardeInstance\"/> <h1>Sauvegarde d'une instance</h1>"
				+ "La sauvegarde permet d'enregistrer une instance pour la conserver. La sauvegarde ne peut se faire qu'avec des instances al�atoires ou alors avec une instance r�elle mais le fichier contenant les positions des noeuds ne sera pas sauvegard�."
				+ "La sauvegarde cr�e une archive <i>sauvegarde.rar</i> � l'emplacement s�lectionn� contenant les 3 fichiers <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i>. <br>"
				+ "Pour pouvoir r�ouvrir une instance pr�c�demment sauvegard�e, il est n�cessaire de d�compresser l'archive pour en extraire les 3 fichiers.<br><br>"
				+ "<u style=\"color: red;\">Attention :</u> Quand une instance est sauvegard�e, les noeuds, les arcs et les commodit�s sont sauvegard�s. En revanche la position des noeuds n'est pas sauvegard�e donc si l'instance est r�-ouverte ult�rieurement, le r�seau affich� ne sera pas le m�me."
				+ "Les r�sultats seront tout de m�me identiques car toutes les valeurs seront sauvegard�es, seul l'affichage sera diff�rent."
				+ "<br><br><br>"
				+ "<a name=\"ouvertureInstance\"/> <h1>Ouverture d'une instance</h1>"
				+ "<h2 style=\"margin-left: 30px;\">Instance r�elle</h2>"
				+ "L'ouverture d'une instance r�elle se fait en s�lectionnant un dossier contenant les 4 fichiers d�crivant une instance r�elle. Ces 4 fichiers sont : <i>fichier_1.csv</i>, <i>fichier_2.csv</i>, <i>fichier_3.csv</i> et <i>position.csv</i>."
				+ "<h2 style=\"margin-left: 30px;\">Instance al�atoire</h2>"
				+ "L'ouverture d'une instance al�atoire se fait en s�lectionnant un dossier contenant les 3 fichiers d�crivant une instance al�atoire � savoir <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i>.<br>"
				+ "A chaque r�ouverture d'une instance al�atoire, la position des noeuds pour l'affichage est modifi�e."
				+ "<br><br><br>"
				+ "<a name=\"resolutionInstance\"/> <h1>R�solution d'une instance</h1>"
				+ "La r�solution d'une instance se fait en cliquant sur le nom d'une m�thode dans le menu <i>Method</i>. Une fois la r�solution termin�e, il est possible de visualiser les r�sultats gr�ce aux diff�rents menus sur la droite de l'�cran."
				+ "La m�me instance peut �tre r�solue plusieurs fois avec des m�thodes diff�rentes. Il est �galement possible de r�soudre une instance plusieurs fois avec la m�me m�thode."
				+ "Dans ce cas, seuls les r�sultats de la derni�re r�solution pourront �tre affich�s.<br>"
				+ "Lorsqu'une nouvelle est ajout�e, son nom est ajout�e dans la liste des m�thodes du menu <i>Method</i>."
				+ "<br><br><br>"
				+ "<a name=\"utilisationGenerale\"/> <h1>Notice g�n�rale</h1>"
				+ "Les dossiers <i>generateur</i>, <i>generateur/res</i> et <i>methods</i> ainsi que le fichier <i>generateur.jl</i> pr�sents dans le r�pertoire d'installation du logiciel sont indispensables � son fonctionnement. Ils ne doivent jamais �tre renomm�s, d�plac�s ou supprim�s.<br>"
				+ "Cette application permet de traiter des instances r�elles et des instances al�atoires. La seule diff�rence entre ces deux types d'instances est la pr�sence d'un fichier <i>position.csv</i> pour les instances r�elles permettant de fixer la position des sommets alors que le positionnement est fait al�atoirement pour les autres instances.<br><br>"
				+ "Dans le cas d'une instance al�atoire, le placement des noeuds pour l'affichage est r�alis� afin d'avoir la meilleure vision possible du r�seau. Il ne refl�te pas forc�ment les valeurs de distances et de co�ts de trajet entre les diff�rents sommets."				
				+ "<br><br><br>"
				+ "<a name=\"menuInformations\"/> <h1>Menu informations</h1>"
				+ "Le menu d'informations est celui sur la droite de l'�cran permettant de visualiser des valeurs et de modifier le mode d'affichage du r�seau.<br>"
				+ "La partie sup�rieure contient tous les contr�les permettant de modifier l'affichage du graphe.<br>"
				+ "<ul>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Zoom carte</code> permet de zoomer sur la carte. 5 niveaux de zoom existent x1, x2, x3, x4, x5.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Mode d'affichage</code> permet de modifier le mode d'affichage des noeuds du graphe. Le mode classique affiche les noeuds en gros avec les num�ros des sommets. Le mode r�duit n'affiche pas les num�ros et r�duit la taille des noeuds. Ce mode permet d'avoir une meilleure vision du r�seau quand il comporte un grand nombre de noeuds.</li>"
				+ "<li>La liste d�roulante suivante permet de s�lectionner les r�sultats obtenus avec quelle m�thode de r�solution doivent �tre affich�s. A chaque fois que l'instance est r�solue avec une nouvelle m�thode, le nom de cette m�thode est ajout� � la liste.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">R�seau base</code> Cocher cette case permet d'afficher en pointill�s gris le r�seau de base. Il s'agit de l'ensemble des arcs existants dans le r�seau avant la s�lection.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">R�seau s�lectionn�</code> Cocher cette case permet d'afficher en vert les arcs qui ont �t� s�lectionn�s lors de la r�solution avec la m�thode actuellement s�lectionn�e. Tous les arcs s�lectionn�s font �galement parti du r�seau de base.</li>"
				+ "<li>La liste d�roulante suivante permet d'afficher le r�sultat d'une commodit�. S�lectionner une commodit� permet d'afficher son trajet avec des arcs orient�s rouges sur le graphe et la longueur de la commodit� est affich�e dans la partie juste en dessous de la liste. Le premier �l�ment de la liste permet de ne plus afficher de commodit�.</li>"
				+ "</ul>"
				+ "La partie inf�rieure de ce menu comporte 2 listes d�roulantes. Ces listes contiennent le nom de toutes les m�thodes avec lesquelles l'instance courante a �t� r�solue. Ce menu permet de comparer le temps de r�solution et la valeur optimale obtenue pour diff�rentes m�thodes de r�solution pour une m�me instance. Le temps d'ex�cution est exprim� en secondes.<br>"
				
				//FORMAT DES FICHIERS
				//INSTALLATION
				//Problemes possibles
				);
		
		
		//Ajout des 2 parties au conteneur principal
		revalidate();		
		add(conteneurG,JSplitPane.LEFT);
		add(conteneurD, JSplitPane.RIGHT);
		//Placement de la barre de s�paration du splitpane � l'ouverture
		setDividerLocation(200);
		//Remonte tout en haut du scroll pane
		contenuDroite.setCaretPosition(0);
		
		//Gestion des clics sur les liens
		contenuDroite.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    		//Quand on clique sur un lien, nous renvoie vers l'ancre associ�e dans la fen�tre de droite
			           contenuDroite.scrollToReference((r.getDescription()));
		         }
		    }
		 });
	}
	
	/**
	 * M�thode permettant de positionner la fenetre sur une ancre.
	 * @param ancre Le nom de l'ancre vis�e.
	 */
	public void goTo(String ancre) {
		contenuDroite.scrollToReference(ancre);
	}
}
