import com.mxgraph.model.mxCell;

/* Représente un noeud du graph avec les coordonnées x,y en pourcentage de la largeur de la fenêtre et l'objet mxCell */

public class Noeud {

	public double x;
	public double y;
	public mxCell node;
	
	public Noeud(mxCell cell) {
		this.node = cell;
	}
	
	public Noeud(mxCell cell, double x, double y) {
		this.x = x;
		this.y = y;
		this.node = cell;
	}
	
}
