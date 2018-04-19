package affichageCentral;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Repr�sente le r�sultat obtenu apr�s r�solution d'une instance.
 * Il peut y avoir plusieurs ResultatInstance pour une m�me instance.
 */
public class ResultatInstance {

	//Sous r�seau utilis� dans ce r�sultat.
	ArrayList<Arc> sousReseau;
	//R�sultats pour chaque commodit� de l'instance.
	HashMap<Integer, CommoditeResultat> commodites;
	//Temps de r�solution en secondes
	public float tempsResolution;
	//Valeur de la fonction objectif
	public String valeurObjectif;
	
	/**
	 * Constructeur
	 */
	public ResultatInstance() {
		this.sousReseau = new ArrayList<Arc>();
		this.commodites = new HashMap<Integer, CommoditeResultat>();
	}
	
	/**
	 *  Ajout d'un arc dans le sous r�seau s�lectionn�
	 *  @param arc Arc � ajouter.
	 */
	public void ajouterSousReseau(Arc arc) {
		sousReseau.add(arc);
	}
	
	/**
	 * Indique si le sous r�seau contient un arc pass� en param�tre ou non.
	 * @param arc Arc que l'on recherche. 
	 * @return true si le sous r�seau contient l'arc, false sinon.
	 */
	public boolean sousReseauContient(Arc arc) {
		return sousReseau.contains(arc);
	}
	
	/**
	 * Ajoute un couple cle/valeur dans la map commodites
	 * @param cle Num�ro de la commodit�.
	 * @param valeur Objet commodit� � ajouter.
	 */
	public void ajouterCommodite(int cle, CommoditeResultat valeur) {
		commodites.put(cle, valeur);
	}
}
