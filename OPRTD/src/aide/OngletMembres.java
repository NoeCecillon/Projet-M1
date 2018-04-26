package aide;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Onglet "Membres" de la fenêtre d'aide
 */
public class OngletMembres extends JScrollPane {

	private JTextPane contentPane;
	
	public OngletMembres() {
		setLayout(new ScrollPaneLayout());
		this.contentPane = new JTextPane();
		contentPane.setEditable(false);
		//Le contenu du textpane sera du contenu html
		contentPane.setContentType("text/html");
		//Le texte à afficher. Mis en forme avec du HTML
		contentPane.setText("<h1>ooo</h1><a href=\"ancre\">aaa</a><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><h1>ooo</h1><br><br><br><br><br><br><br><br><br><br><a name=\"ancre\"/><h1>ooo2</h1><br><br><br><br><br><br><br><br><br><br>");
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
