package affichageCentral;
import com.mxgraph.model.mxCell;

/**
 *  Repr�sente un noeud du graphe avec les coordonn�es x,y en pourcentage de la largeur de la fen�tre et l'objet mxCell 
 */

public class Noeud {

	// x et y sont les pourcentages entre 0 et 1. Un sommet en plein milieu de la fen�tre aura x=0.5 et y=0.5.
	public double x;
	public double y;
	public mxCell node;
	
	/**
	 * Constructeur 
	 * @param cell L'objet repr�sentant le noeud.
	 */
	public Noeud(mxCell cell) {
		this.node = cell;
	}
}
