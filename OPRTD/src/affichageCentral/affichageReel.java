package affichageCentral;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import com.mxgraph.swing.mxGraphComponent;

public class affichageReel extends PanelCentre {
	
	// L'instance qui doit être traitée.
	Instance instance;
	
	/**
	 * Construit un affichage réel. Instancie les objets et ajoute le mxGraphComponent au panel pour l'afficher.
	 * @param path Chemin vers le dossier contenant les fichiers de l'instance à traiter.
	 */
	public affichageReel(String path) {
		setPreferredSize(new Dimension(100,100));
		setLayout(new BorderLayout());	
		this.instance = new Instance(true, path);		  		 
		this.comp = new mxGraphComponent(instance);		  		
		add(comp, BorderLayout.CENTER);
		//Met en place le listener pour redimensionner la carte au redimensionnement de la fenêtre.
		redimensionnementAuto();		  	  
	}
	
	/**
	 *  Listener pour replacer les noeuds au redimensionnement de la fenêtre 
	 */
	public void redimensionnementAuto() {
		this.comp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {    	
            	instance.replacerNoeuds(getWidth(), getHeight());
                repaint();
            }          
        });
	}
	
	/**
	 * Getter de l'instance.
	 * @return L'instance traitée.
	 */
	public Instance getInstance() {
		return this.instance;
	}
}
