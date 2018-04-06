package affichageCentral;
import com.mxgraph.model.mxCell;

/** Représente un arc présent dans le graphe avec origine, destination et l'objet mxCell représentant cet arc. 
 */
public class Arc {
	
	public int origine;
	public int destination;
	public mxCell arc;
	
	/**
	 * Constructeur avec tous les paramètres. 
	 * Utilisé pendant la création des instances quand on crée directement l'objet mxCell.
	 * @param o Sommet d'origine.
	 * @param d Sommet de destination.
	 * @param a Objet arc.
	 */
	public Arc(int o, int d, mxCell a) {
		this.origine = o;
		this.destination = d;
		this.arc = a;
	}
	
	/**
	 * Constructeur avec seulement 2 paramètres.
	 * Utilisé pendant la création des commodités quand l'objet arc n'existe pas encore.
	 * @param o Sommet d'origine.
	 * @param d Sommet de destination.
	 */
	public Arc(int o, int d) {
		this.origine = o;
		this.destination = d;
	}
	
	/**
	 * Permet de vérifier si un sommet est l'origine ou la destination de cet arc.
	 * De plus, si il est l'un des 2, on place ce sommet comme étant la destination de l'arc.
	 * @see CommoditeResultat#ordonnerArcs() pour l'explication.
	 * @param val Le numéro du sommet dont on veut vérifier si il est l'origine ou la destination de cet arc.
	 * @return true si le paramètre est l'origine ou la destination de cet arc, false sinon.
	 */
	public boolean contains(int val) {
		if (origine == val || destination == val) {
			/* On place le sommet recherché comme étant le sommet de destination */
			if (destination != val) {
				int tmp = destination;
				destination = origine;
				origine = tmp;
			}
			return true;
		}
		return false;
	}
}
