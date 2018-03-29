import com.mxgraph.model.mxCell;

/* Repr�sente un noeud du graph avec les coordonn�es x,y en pourcentage de la largeur de la fen�tre et l'objet mxCell */

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
