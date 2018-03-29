import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;

public class affichageRandom extends PanelCentre {

	Instance instance;
	
	
	public affichageRandom(String path) {
		this.instance = new Instance(false, path);		
		setPreferredSize(new Dimension(100,100));
		setLayout(new BorderLayout());
		this.comp = new mxGraphComponent(instance);
		add(comp, BorderLayout.CENTER);
		
		redimensionnementAuto();
	}
	
	/* Constructeur pour créer l'instance au tout début de l'application (argument juste pour différencier de l'autre) */
	public affichageRandom(int i) {
	}

	/* Listener pour replacer les noeuds au redimensionnement de la fenêtre */
	public void redimensionnementAuto() {
		this.comp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {    	
            	instance.replacerNoeuds(getWidth(), getHeight());
                repaint();
            }          
        });
	}
	
	public Instance getInstance() {
		return this.instance;
	}
}
