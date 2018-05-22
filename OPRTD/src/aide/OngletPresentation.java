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
		//Le texte � afficher. Mis en forme avec du HTML
		
		contentPane.setText(
//"<img src=\""+getClass().getClassLoader().getResource("img/logo_dangerzone.png").toString()+"\" width=\"150\" height=\"150\" hspace=\"40\" vspace=\"40\">"
//+ "DANGERZONE"
//+ "<hr width=\"75%\"><br>"	
 "<font face=\"Verdana\">"
+ "Ce projet s\'inscrit dans le cadre de la premi�re ann�e de Master Informatique pr�par� au Centre de Recherche en Informatique � l\'Universit� d\'Avignon. Le premier semestre est consacr� � la r�daction"
+" d\'un cahier des charges et � l\'�tude de faisabilit� du programme demand�. <br><br>"
+" L\'objectif de ce projet est de cr�er une interface graphique permettant de donner une repr�sentation"
+" visuelle rapidement et facilement de la solution d\'un probl�me d\'optimisation dans un r�seau de"
+" transport. Ce probl�me consiste � rechercher un sous ensemble d\'ar�tes dans un graphe repr�sentant"
+" un r�seau routier afin que des produits dangereux puissent �tre transport�s d\'un point de d�part"	
+" vers un point d\'arriv�e en suivant un certain nombre de contraintes. Ce probl�me est l\'objet de la"
+" th�se de Ikram BOURAS (doctorante au LIA et au Laboratoire d\'informatique, de robotique et de"
+" micro�lectronique de Montpellier) qui est la \"cliente\" de notre projet. Dans le cadre de celui-ci, nous"
+" ne nous occuperons donc pas de trouver des m�thodes pour r�soudre ce probl�me. Tous les mod�les"
+" que nous utiliserons afin de trouver des solutions au probl�me d\'optimisation seront ceux d�velopp�s"
+" par Ikram BOURAS dans le cadre de sa th�se et qui nous seront fournis. Ces m�thodes permettent"
+" d\'obtenir des r�sultats � partir d\'une instance de probl�me. Une instance est un exemple de probl�me"
+" qui comporte un r�seau routier repr�sent� par un graphe, un ensemble de couples point de d�part"
+" / point d\'arriv�e, des co�ts fixes et variables , etc... Les instances que nous utiliserons pourront"
+" �tre r�elles, c\'est � dire bas�es sur des donn�es r�elles d\'un r�seau routier qui existe, ou alors des"
+" instances g�n�r�es al�atoirement qui permettent de pouvoir effectuer des tests sur un nombre illimit� d\'instances. <br><br>"
+" Notre projet a donc pour objectif de fournir une application permettant de faciliter l\'utilisation"
+" des outils d�velopp�s par Ikram, de rendre les r�sultats plus simples � visualiser et � comprendre, et"
+" de mettre en valeur le travail effectu� par Ikram durant sa th�se."
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
