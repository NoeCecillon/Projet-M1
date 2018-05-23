package aide;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class OngletMembres extends JScrollPane {

private JTextPane contentPane;
	
	/**
	 * Crée l'onglet listant les membres de l'équipe de projet.
	 */
	public OngletMembres() {
		setLayout(new ScrollPaneLayout());
		this.contentPane = new JTextPane();
		contentPane.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contentPane.setContentType("text/html");
		//Le texte à afficher. Mis en forme avec du HTML
		
		contentPane.setText(
"<h1>Equipe 2017-2018</h1><br>"
+ "<ul>"
+ "<li><b style=\"font-size: 15px;\">Thomas BRINGER</b></li>"
+ "<li><b style=\"font-size: 15px;\">Noé CECILLON</b></li>"
+ "</ul>"
				
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
