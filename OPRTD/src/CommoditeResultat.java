import java.util.ArrayList;

/* Repr�sente une commodite avec des r�sultats. Utilis� dans ResultatInstance */
public class CommoditeResultat extends Commodite {

	public ArrayList<Arc> arcsUtilises;
	public double length;
	
	public CommoditeResultat(int dep, int arr) {
		super(dep, arr);
		this.arcsUtilises = new ArrayList<Arc>();
	}
	
	/* Ajout d'un arc utilis� */
	public void addArc(Arc arc) {
		this.arcsUtilises.add(arc);	
	}
	
	/* Permet de mettre les arcs utilis�s dans l'ordre du d�part vers l'arriv�e */
	/* De base les arcs ne sont pas forc�ment dans le bon ordre et les sommets origine/destination peuvent �tre invers�s */
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
