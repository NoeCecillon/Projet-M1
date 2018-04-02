import java.util.ArrayList;
import java.util.HashMap;

public class ResultatInstance {

	ArrayList<Arc> sousReseau;
	HashMap<Integer, CommoditeResultat> commodites;
	
	public ResultatInstance() {
		this.sousReseau = new ArrayList<Arc>();
		this.commodites = new HashMap<Integer, CommoditeResultat>();
	}
	
	/* Ajout d'un arc dans le sous réseau sélectionné */
	public void ajouterSousReseau(Arc arc) {
		sousReseau.add(arc);
	}
	
	/* Indique si le sous réseau contient un arc ou non */
	public boolean sousReseauContient(Arc a) {
		return sousReseau.contains(a);
	}
	
	/* Ajoute un couple cle/valeur dans la map commodites */
	public void ajouterCommodite(int cle, CommoditeResultat valeur) {
		commodites.put(cle, valeur);
	}
}
