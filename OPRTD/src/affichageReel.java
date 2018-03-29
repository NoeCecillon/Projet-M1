import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import com.mxgraph.swing.mxGraphComponent;

public class affichageReel extends PanelCentre {
	
	Instance instance;
	mxGraphComponent comp;
	
	public affichageReel(String path) {

		setPreferredSize(new Dimension(100,100));
		setLayout(new BorderLayout());
		
		this.instance = new Instance(true, path);		  		 
		this.comp = new mxGraphComponent(instance);		  
		
		add(comp, BorderLayout.CENTER);
		
		redimensionnementAuto();		  	  
	}
	
	/* Listener pour replacer les noeuds au redimensionnement de la fen�tre */
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
