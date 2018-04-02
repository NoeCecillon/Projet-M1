import java.awt.BorderLayout;

import javax.swing.JSplitPane;

public class FenetreInfos extends JSplitPane {
	
	FenetreInfosHaut haut;
	FenetreInfosBas bas;
	
	public FenetreInfos(Instance i) {
		/* La fenêtre est séparée en 2 en hauteur */
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		this.haut = new FenetreInfosHaut(i, this);		
		add(this.haut, JSplitPane.TOP);
		this.bas = new FenetreInfosBas();		
		add(this.bas, JSplitPane.BOTTOM);
	}
	
	public FenetreInfosBas getBas() {
		return this.bas;
	}
	
	/* Désactive les controles */
	public void activer() {
		this.haut.activer();
	}
	
	/* Active les controles */
	public void desactiver() {
		this.haut.desactiver();
	}
}