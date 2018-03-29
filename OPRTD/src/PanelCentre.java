import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;

public abstract class PanelCentre extends JPanel {

	mxGraphComponent comp;
	
	abstract void redimensionnementAuto();
	abstract Instance getInstance();
}
