package affichageCentral;

/**
 *  Représente une commodité avec simplement le départ et l'arrivée. 
 *  Utilisé pour représenter les commodités d'une instance. 
 */
public class Commodite {

	public int depart;
	public int arrivee;
	
	/**
	 * Constructeur
	 * @param dep Sommet de départ.
	 * @param arr Sommet d'arrivée.
	 */
	public Commodite(int dep, int arr) {
		this.depart = dep;
		this.arrivee = arr;
	}
}
