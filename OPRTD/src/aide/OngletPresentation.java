package aide;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class OngletPresentation extends JScrollPane {

private JTextPane contentPane;
	
	public OngletPresentation() {
		setLayout(new ScrollPaneLayout());
		this.contentPane = new JTextPane();
		contentPane.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contentPane.setContentType("text/html");
		//Le texte à afficher. Mis en forme avec du HTML
		
		contentPane.setText(
//"<img src=\""+getClass().getClassLoader().getResource("img/logo_dangerzone.png").toString()+"\" width=\"150\" height=\"150\" hspace=\"40\" vspace=\"40\">"
//+ "DANGERZONE"
//+ "<hr width=\"75%\"><br>"	
 "<font face=\"Verdana\">"
+ "Ce projet s\'inscrit dans le cadre de la première année de Master Informatique préparé au Centre de Recherche en Informatique à l\'Université d\'Avignon. Le premier semestre est consacré à la rédaction"
+" d\'un cahier des charges et à l\'étude de faisabilité du programme demandé. <br><br>"
+" L\'objectif de ce projet est de créer une interface graphique permettant de donner une représentation"
+" visuelle rapidement et facilement de la solution d\'un problème d\'optimisation dans un réseau de"
+" transport. Ce problème consiste à rechercher un sous ensemble d\'arêtes dans un graphe représentant"
+" un réseau routier afin que des produits dangereux puissent être transportés d\'un point de départ"	
+" vers un point d\'arrivée en suivant un certain nombre de contraintes. Ce problème est l\'objet de la"
+" thèse de Ikram BOURAS (doctorante au LIA et au Laboratoire d\'informatique, de robotique et de"
+" microélectronique de Montpellier) qui est la \"cliente\" de notre projet. Dans le cadre de celui-ci, nous"
+" ne nous occuperons donc pas de trouver des méthodes pour résoudre ce problème. Tous les modèles"
+" que nous utiliserons afin de trouver des solutions au problème d\'optimisation seront ceux développés"
+" par Ikram BOURAS dans le cadre de sa thèse et qui nous seront fournis. Ces méthodes permettent"
+" d\'obtenir des résultats à partir d\'une instance de problème. Une instance est un exemple de problème"
+" qui comporte un réseau routier représenté par un graphe, un ensemble de couples point de départ"
+" / point d\'arrivée, des coûts fixes et variables , etc... Les instances que nous utiliserons pourront"
+" être réelles, c\'est à dire basées sur des données réelles d\'un réseau routier qui existe, ou alors des"
+" instances générées aléatoirement qui permettent de pouvoir effectuer des tests sur un nombre illimité d\'instances. <br><br>"
+" Notre projet a donc pour objectif de fournir une application permettant de faciliter l\'utilisation"
+" des outils développés par Ikram, de rendre les résultats plus simples à visualiser et à comprendre, et"
+" de mettre en valeur le travail effectué par Ikram durant sa thèse."
+ "</font>"
				
				);
		
		contentPane.setBounds(0, 0, 10000, 10000);
		//Renvoie vers le haut de la page
				contentPane.scrollToReference("top");
		setViewportView(contentPane);
		revalidate();
		
		contentPane.addHyperlinkListener(new HyperlinkListener()
		{
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			        System.out.println(r.getDescription());
			            
			           contentPane.scrollToReference((r.getDescription()));
		         }
		    }
		 });
	}
}
