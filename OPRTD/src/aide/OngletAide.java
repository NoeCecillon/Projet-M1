package aide;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Onglet "Aide" de la fenêtre d'aide. Contient toute l'aide pour utiliser l'application.
 */
public class OngletAide extends JSplitPane {

	//Contient le texte d'aide
	private JTextPane contenuDroite;
	
	public OngletAide() {
		//La fenêtre est séparée en 2 parties : à droite le contenu et à gauche le sommaire
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
		//Le texte à afficher. Mis en forme avec du HTML
		contenuGauche.setText(
				"<a href=\"selectionExecutable\">Sélection de l'exécutable Julia</a><br>"
				+ "<a href=\"ajoutMethode\">Ajout d'une méthode de résolution</a><br>"
				+ "<a href=\"generationInstance\">Génération d'une instance aléatoire</a><br>"
				+ "<a href=\"sauvegardeInstance\">Sauvegarde d'une instance</a><br>"
				+ "<a href=\"ouvertureInstance\">Ouverture d'une instance</a><br>"
				+ "<a href=\"resolutionInstance\">Résolution d'une instance</a><br>"
				+ "<a href=\"utilisationGenerale\">Notice générale</a><br>"
				+ "<a href=\"menuInformations\">Menu informations</a><br>"
				);
		
		//Gestion des clics sur les liens
		contenuGauche.addHyperlinkListener(new HyperlinkListener()
		{
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    		//Quand on clique sur un lien, nous renvoie vers l'ancre associée dans la fenêtre de droite
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
		//Le texte à afficher. Mis en forme avec du HTML
		// L'attribut 'id' en HTML est remplacé par l'attribut 'name'
		contenuDroite.setText(
				"<a name=\"top\"/>"
				+ "<a name=\"selectionExecutable\"/> <h1>Sélection de l'exécutable Julia</h1>"
				+ "Lors du premier lancement de l'application ou si le fichier <i>config.txt</i> a été supprimé, il sera nécessaire d'indiquer l'emplacement de l'exécutable Julia que l'application doit utiliser."
				+ "Il s'agit du fichier <i>julia.exe</i> se trouvant la plupart du temps dans <i>[Dossier d'installation de Julia]/bin/julia.exe</i>.<br>"
				+ "Le fichier choisi est sauvegardé et il n'y aura donc plus besoin de faire cette manipulation lors des utilisations suivantes.<br><br>"
				+ "En cas d'erreur lors de la sélection de ce fichier, supprimez le fichier <i>config.txt</i> dans le dossier d'installation d'OPRTD et relancez l'application pour pouvoir sélectionner le fichier à nouveau."
				+ "<br><br><br>"
				+ "<a name=\"ajoutMethode\"/> <h1>Ajout d'une méthode de résolution</h1>"
				+ "Il est possible d'ajouter de nouvelles méthodes de résolution. Les méthodes ajoutées doivent suivre les conditions suivantes pour avoir un fonctionnement correct de l'application : <br>"
				+ "<ul>"
				+ "<li>En début de fichier, juste après les <code style=\"background-color:#d9d9db;\">using XXX</code>, il faut ajouter la ligne suivante <code style=\"background-color:#d9d9db;\">File=string(ARGS[1])</code>.</li>"
				+ "<li>Les chemins vers les fichiers <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i> doivent être remplacés par <code style=\"background-color:#d9d9db;\">File*\"fichier_3.csv\"</code>.</li>"
				+ "<li>Le sous réseau sélectionné doit être affiché en commençant par <code style=\"background-color:#d9d9db;\">subgraph :</code> puis avec tous les arcs sous la forme  <code style=\"background-color:#d9d9db;\">(27,20)   (57,27)   (58,57)   (64,50) ...</code></li>"
				+ "<li>Le résultat des commodités doit être affiché en commençant par <code style=\"background-color:#d9d9db;\">commodity X :</code> ou X est le numéro de la commodité et suivi des sommets utilisés par cette commodité sous la forme <code style=\"background-color:#d9d9db;\">commodity 1 :    (77,79)   (79,111)   (80,77) ...</code></li>"
				+ "<li>La ligne suivante doit contenir la longueur de la commodité sous la forme <code style=\"background-color:#d9d9db;\">length : 3203.966569</code></li>"
				+ "</ul>"
				+ "Les méthodes ajoutées sont sauvegardées et pourront être utilisées lors des prochains lancements du logiciel. Le nom des méthodes qui sera utilisé dans le logiciel est le nom du fichier. Une fois qu'une méthode a été ajoutée, le fichier .jl correspondant est copié dans le dossier <i>methods</i> contenu dans le répertoire d'installation d'OPRTD. Il est possible de modifier directement ce fichier ou de le supprimer pour ne plus le voir dans l'application."
				+ "<br><br><br>"
				+ "<a name=\"generationInstance\"/> <h1>Génération d'une instance aléatoire</h1>"
				+ "Il est possible de générer une instance aléatoire en choisissant un certain nombre de paramètres. Ces paramètres sont : <br>"
				+ "<ul>"
				+ "<li>Nombre de noeuds : un entier représentant le nombre de noeuds à créer dans l'instance générée. Il est conseillé d'en mettre au moins 8.</li>"
				+ "<li>Densité : la densité du réseau qui doit être généré. Il s'agit d'un nombre compris entre 0 et 1. Il est conseillé de ne pas utiliser de valeur trop proche de 0 ou de 1.</li>"
				+ "<li>Angle alpha</li>"
				+ "<li>Nombre de commodités : un entier représentant le nombre de commodités à créer. Il est conseillé d'en mettre au moins 8.</li>"
				+ "</ul>"
				+ "Lorsqu'une instance est générée, 3 fichiers sont générés dans le dossier <i>generateur/res</i> du répertoire d'installation du logiciel. "
				+ "A chaque nouvelle génération, ces fichiers sont réécrits. Pour ne pas perdre une instance générée, il est possible de la <a href=\"sauvegardeInstance\">sauvegarder</a>. <br>"
				+ "Le fichier du générateur est accessible dans le dossier <i>generateur</i> mais il ne doit jamais être supprimé.<br><br>"
				+ "Il peut arriver que la génération n'aboutisse jamais notamment à cause de valeurs trop petites ou trop grandes. Dans ce cas, fermez l'application et relancez la."				
				+ "<br><br><br>"
				+ "<a name=\"sauvegardeInstance\"/> <h1>Sauvegarde d'une instance</h1>"
				+ "La sauvegarde permet d'enregistrer une instance pour la conserver. La sauvegarde ne peut se faire qu'avec des instances aléatoires ou alors avec une instance réelle mais le fichier contenant les positions des noeuds ne sera pas sauvegardé."
				+ "La sauvegarde crée une archive <i>sauvegarde.rar</i> à l'emplacement sélectionné contenant les 3 fichiers <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i>. <br>"
				+ "Pour pouvoir réouvrir une instance précédemment sauvegardée, il est nécessaire de décompresser l'archive pour en extraire les 3 fichiers.<br><br>"
				+ "<u style=\"color: red;\">Attention :</u> Quand une instance est sauvegardée, les noeuds, les arcs et les commodités sont sauvegardés. En revanche la position des noeuds n'est pas sauvegardée donc si l'instance est ré-ouverte ultérieurement, le réseau affiché ne sera pas le même."
				+ "Les résultats seront tout de même identiques car toutes les valeurs seront sauvegardées, seul l'affichage sera différent."
				+ "<br><br><br>"
				+ "<a name=\"ouvertureInstance\"/> <h1>Ouverture d'une instance</h1>"
				+ "<h2 style=\"margin-left: 30px;\">Instance réelle</h2>"
				+ "L'ouverture d'une instance réelle se fait en sélectionnant un dossier contenant les 4 fichiers décrivant une instance réelle. Ces 4 fichiers sont : <i>fichier_1.csv</i>, <i>fichier_2.csv</i>, <i>fichier_3.csv</i> et <i>position.csv</i>."
				+ "<h2 style=\"margin-left: 30px;\">Instance aléatoire</h2>"
				+ "L'ouverture d'une instance aléatoire se fait en sélectionnant un dossier contenant les 3 fichiers décrivant une instance aléatoire à savoir <i>fichier_1.csv</i>, <i>fichier_2.csv</i> et <i>fichier_3.csv</i>.<br>"
				+ "A chaque réouverture d'une instance aléatoire, la position des noeuds pour l'affichage est modifiée."
				+ "<br><br><br>"
				+ "<a name=\"resolutionInstance\"/> <h1>Résolution d'une instance</h1>"
				+ "La résolution d'une instance se fait en cliquant sur le nom d'une méthode dans le menu <i>Method</i>. Une fois la résolution terminée, il est possible de visualiser les résultats grâce aux différents menus sur la droite de l'écran."
				+ "La même instance peut être résolue plusieurs fois avec des méthodes différentes. Il est également possible de résoudre une instance plusieurs fois avec la même méthode."
				+ "Dans ce cas, seuls les résultats de la dernière résolution pourront être affichés.<br>"
				+ "Lorsqu'une nouvelle est ajoutée, son nom est ajoutée dans la liste des méthodes du menu <i>Method</i>."
				+ "<br><br><br>"
				+ "<a name=\"utilisationGenerale\"/> <h1>Notice générale</h1>"
				+ "Les dossiers <i>generateur</i>, <i>generateur/res</i> et <i>methods</i> ainsi que le fichier <i>generateur.jl</i> présents dans le répertoire d'installation du logiciel sont indispensables à son fonctionnement. Ils ne doivent jamais être renommés, déplacés ou supprimés.<br>"
				+ "Cette application permet de traiter des instances réelles et des instances aléatoires. La seule différence entre ces deux types d'instances est la présence d'un fichier <i>position.csv</i> pour les instances réelles permettant de fixer la position des sommets alors que le positionnement est fait aléatoirement pour les autres instances.<br><br>"
				+ "Dans le cas d'une instance aléatoire, le placement des noeuds pour l'affichage est réalisé afin d'avoir la meilleure vision possible du réseau. Il ne reflète pas forcément les valeurs de distances et de coûts de trajet entre les différents sommets."				
				+ "<br><br><br>"
				+ "<a name=\"menuInformations\"/> <h1>Menu informations</h1>"
				+ "Le menu d'informations est celui sur la droite de l'écran permettant de visualiser des valeurs et de modifier le mode d'affichage du réseau.<br>"
				+ "La partie supérieure contient tous les contrôles permettant de modifier l'affichage du graphe.<br>"
				+ "<ul>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Zoom carte</code> permet de zoomer sur la carte. 5 niveaux de zoom existent x1, x2, x3, x4, x5.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Mode d'affichage</code> permet de modifier le mode d'affichage des noeuds du graphe. Le mode classique affiche les noeuds en gros avec les numéros des sommets. Le mode réduit n'affiche pas les numéros et réduit la taille des noeuds. Ce mode permet d'avoir une meilleure vision du réseau quand il comporte un grand nombre de noeuds.</li>"
				+ "<li>La liste déroulante suivante permet de sélectionner les résultats obtenus avec quelle méthode de résolution doivent être affichés. A chaque fois que l'instance est résolue avec une nouvelle méthode, le nom de cette méthode est ajouté à la liste.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Réseau base</code> Cocher cette case permet d'afficher en pointillés gris le réseau de base. Il s'agit de l'ensemble des arcs existants dans le réseau avant la sélection.</li>"
				+ "<li><code style=\"background-color:#d9d9db; margin-left: 30px;\">Réseau sélectionné</code> Cocher cette case permet d'afficher en vert les arcs qui ont été sélectionnés lors de la résolution avec la méthode actuellement sélectionnée. Tous les arcs sélectionnés font également parti du réseau de base.</li>"
				+ "<li>La liste déroulante suivante permet d'afficher le résultat d'une commodité. Sélectionner une commodité permet d'afficher son trajet avec des arcs orientés rouges sur le graphe et la longueur de la commodité est affichée dans la partie juste en dessous de la liste. Le premier élément de la liste permet de ne plus afficher de commodité.</li>"
				+ "</ul>"
				+ "La partie inférieure de ce menu comporte 2 listes déroulantes. Ces listes contiennent le nom de toutes les méthodes avec lesquelles l'instance courante a été résolue. Ce menu permet de comparer le temps de résolution et la valeur optimale obtenue pour différentes méthodes de résolution pour une même instance. Le temps d'exécution est exprimé en secondes.<br>"
				
				//FORMAT DES FICHIERS
				//INSTALLATION
				//Problemes possibles
				);
		
		
		//Ajout des 2 parties au conteneur principal
		revalidate();		
		add(conteneurG,JSplitPane.LEFT);
		add(conteneurD, JSplitPane.RIGHT);
		//Placement de la barre de séparation du splitpane à l'ouverture
		setDividerLocation(200);
		//Remonte tout en haut du scroll pane
		contenuDroite.setCaretPosition(0);
		
		//Gestion des clics sur les liens
		contenuDroite.addHyperlinkListener(new HyperlinkListener() {
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    		//Quand on clique sur un lien, nous renvoie vers l'ancre associée dans la fenêtre de droite
			           contenuDroite.scrollToReference((r.getDescription()));
		         }
		    }
		 });
	}
	
	/**
	 * Méthode permettant de positionner la fenetre sur une ancre.
	 * @param ancre Le nom de l'ancre visée.
	 */
	public void goTo(String ancre) {
		contenuDroite.scrollToReference(ancre);
	}
}
