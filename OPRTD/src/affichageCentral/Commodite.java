package affichageCentral;

/**
 *  Repr�sente une commodit� avec simplement le d�part et l'arriv�e. 
 *  Utilis� pour repr�senter les commodit�s d'une instance. 
 */
public class Commodite {

	public int depart;
	public int arrivee;
	
	/**
	 * Constructeur
	 * @param dep Sommet de d�part.
	 * @param arr Sommet d'arriv�e.
	 */
	public Commodite(int dep, int arr) {
		this.depart = dep;
		this.arrivee = arr;
	}
}
