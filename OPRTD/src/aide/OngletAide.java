package aide;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Onglet "Aide" de la fen�tre d'aide. Contient toute l'aide pour utiliser l'application.
 */
public class OngletAide extends JScrollPane {

	private JTextPane contentPane;
	
	public OngletAide() {
		setLayout(new ScrollPaneLayout());
		this.contentPane = new JTextPane();
		contentPane.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contentPane.setContentType("text/html");
		//Le texte � afficher. Mis en forme avec du HTML
		contentPane.setText("<h1>ooo</h1><a name=\"top\">aaa</a><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><a name=\"ancre\"/><h1>ooo2</h1><br><br><br><br><br><br><br><br><br><br>");
		contentPane.setBounds(0, 0, 10000, 10000);
		//Renvoie vers le haut de la page
		contentPane.scrollToReference("top");
		setViewportView(contentPane);
		revalidate();
		
		/*contentPane.addHyperlinkListener(new HyperlinkListener()
		{
		    public void hyperlinkUpdate(HyperlinkEvent r){
		    	if(r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			        System.out.println(r.getDescription());
			            
			           contentPane.scrollToReference((r.getDescription()));
		         }
		    }
		 });*/
	}
	
	/**
	 * M�thode permettant de positionner la fenetre sur une ancre.
	 * @param ancre Le nom de l'ancre vis�e.
	 */
	public void goTo(String ancre) {
		contentPane.scrollToReference(ancre);
	}
}
