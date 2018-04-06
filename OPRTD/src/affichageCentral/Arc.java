package affichageCentral;
import com.mxgraph.model.mxCell;

/** Repr�sente un arc pr�sent dans le graphe avec origine, destination et l'objet mxCell repr�sentant cet arc. 
 */
public class Arc {
	
	public int origine;
	public int destination;
	public mxCell arc;
	
	/**
	 * Constructeur avec tous les param�tres. 
	 * Utilis� pendant la cr�ation des instances quand on cr�e directement l'objet mxCell.
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
	 * Constructeur avec seulement 2 param�tres.
	 * Utilis� pendant la cr�ation des commodit�s quand l'objet arc n'existe pas encore.
	 * @param o Sommet d'origine.
	 * @param d Sommet de destination.
	 */
	public Arc(int o, int d) {
		this.origine = o;
		this.destination = d;
	}
	
	/**
	 * Permet de v�rifier si un sommet est l'origine ou la destination de cet arc.
	 * De plus, si il est l'un des 2, on place ce sommet comme �tant la destination de l'arc.
	 * @see CommoditeResultat#ordonnerArcs() pour l'explication.
	 * @param val Le num�ro du sommet dont on veut v�rifier si il est l'origine ou la destination de cet arc.
	 * @return true si le param�tre est l'origine ou la destination de cet arc, false sinon.
	 */
	public boolean contains(int val) {
		if (origine == val || destination == val) {
			/* On place le sommet recherch� comme �tant le sommet de destination */
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
