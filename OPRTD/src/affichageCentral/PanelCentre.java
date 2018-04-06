package affichageCentral;
import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;

/**
 * L'objet qui est au centre de la fenetre principale. Peut repr�senter une instance r�elle ou random.
 */
public abstract class PanelCentre extends JPanel {

	mxGraphComponent comp;
	
	abstract void redimensionnementAuto();
	public abstract Instance getInstance();
}
