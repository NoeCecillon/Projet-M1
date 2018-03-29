import java.util.ArrayList;

public class Commodite {

	public int depart;
	public int arrivee;
	public ArrayList<Arc> arcsUtilises;
	public double length;
	
	public Commodite(int dep, int arr) {
		this.depart = dep;
		this.arrivee = arr;
		this.arcsUtilises = new ArrayList<Arc>();
	}
	
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
		/*System.out.print(depart + " " + arrivee + " ");
		for (int i=0; i<arcsUtilises.size(); i++) {
			System.out.print(arcsUtilises.get(i) + " ");
		}
		System.out.println();*/
	}
}
