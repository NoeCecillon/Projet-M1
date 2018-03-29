import java.awt.Component;

import javax.swing.JPanel;

public class FenetreInfosBas extends JPanel {

	public FenetreInfosBas() {
	}
	
	public void vider() {
		this.removeAll();
	}
	
	public void ajouter(Component comp) {
		add(comp);
	}
}
