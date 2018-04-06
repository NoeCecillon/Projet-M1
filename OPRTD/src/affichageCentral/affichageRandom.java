package affichageCentral;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;

/**
 * Affichage pour une instance random.
 */

public class affichageRandom extends PanelCentre {

	// L'instance qui doit �tre trait�e.
	Instance instance;
	
	/**
	 * Construit un affichage random. Instancie les objets et ajoute le mxGraphComponent au panel pour l'afficher.
	 * @param path Chemin vers le dossier contenant les fichiers de l'instance � traiter.
	 */
	public affichageRandom(String path) {
		this.instance = new Instance(false, path);		
		setPreferredSize(new Dimension(100,100));
		setLayout(new BorderLayout());
		this.comp = new mxGraphComponent(instance);
		add(comp, BorderLayout.CENTER);
		//Met en place le listener pour redimensionner la carte au redimensionnement de la fen�tre.
		redimensionnementAuto();
	}
	
	/**
	 * Constructeur pour cr�er l'instance au tout d�but de l'application (argument juste pour diff�rencier de l'autre). 
	 * @param i N'importe quoi.
	 */
	public affichageRandom(int i) {
	}

	/**
	 *  Listener pour replacer les noeuds au redimensionnement de la fen�tre 
	 */
	public void redimensionnementAuto() {
		this.comp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {   
            	//On donne la largeur et la hauteur du panel pour redimensionner correctement.
            	instance.replacerNoeuds(getWidth(), getHeight());
                repaint();
            }          
        });
	}
	
	/**
	 * Getter de l'instance.
	 * @return L'instance trait�e.
	 */
	public Instance getInstance() {
		return this.instance;
	}
}
