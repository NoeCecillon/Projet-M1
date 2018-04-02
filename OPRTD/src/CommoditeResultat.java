import java.util.ArrayList;

/* Représente une commodite avec des résultats. Utilisé dans ResultatInstance */
public class CommoditeResultat extends Commodite {

	public ArrayList<Arc> arcsUtilises;
	public double length;
	
	public CommoditeResultat(int dep, int arr) {
		super(dep, arr);
		this.arcsUtilises = new ArrayList<Arc>();
	}
	
	/* Ajout d'un arc utilisé */
	public void addArc(Arc arc) {
		this.arcsUtilises.add(arc);	
	}
	
	/* Permet de mettre les arcs utilisés dans l'ordre du départ vers l'arrivée */
	/* De base les arcs ne sont pas forcément dans le bon ordre et les sommets origine/destination peuvent être inversés */
	public void ordonnerArcs() {
		ArrayList<Arc> tempo = (ArrayList<Arc>) arcsUtilises.clone();
		arcsUtilises.clear();
		int nb = arrivee;
		Arc selected = null;
		
		while (!tempo.isEmpty()) {
			//parcours de tous les arcs
			for(int i=0; i<tempo.size(); i++) {
				Arc a = tempo.get(i);
				if (a.contains(nb)){
					selected = a;
				}
			}
			this.arcsUtilises.add(0, selected);
			tempo.remove(selected);
			nb = selected.origine;
		}
	}

}
