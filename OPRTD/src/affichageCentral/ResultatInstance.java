package affichageCentral;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Représente le résultat obtenu après résolution d'une instance.
 * Il peut y avoir plusieurs ResultatInstance pour une même instance.
 */
public class ResultatInstance {

	//Sous réseau utilisé dans ce résultat.
	ArrayList<Arc> sousReseau;
	//Résultats pour chaque commodité de l'instance.
	HashMap<Integer, CommoditeResultat> commodites;
	//Temps de résolution en secondes
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
	 *  Ajout d'un arc dans le sous réseau sélectionné
	 *  @param arc Arc à ajouter.
	 */
	public void ajouterSousReseau(Arc arc) {
		sousReseau.add(arc);
	}
	
	/**
	 * Indique si le sous réseau contient un arc passé en paramètre ou non.
	 * @param arc Arc que l'on recherche. 
	 * @return true si le sous réseau contient l'arc, false sinon.
	 */
	public boolean sousReseauContient(Arc arc) {
		return sousReseau.contains(arc);
	}
	
	/**
	 * Ajoute un couple cle/valeur dans la map commodites
	 * @param cle Numéro de la commodité.
	 * @param valeur Objet commodité à ajouter.
	 */
	public void ajouterCommodite(int cle, CommoditeResultat valeur) {
		commodites.put(cle, valeur);
	}
}
