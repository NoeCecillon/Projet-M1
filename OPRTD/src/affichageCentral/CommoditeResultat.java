package affichageCentral;
import java.util.ArrayList;

/**
 *  Représente une commodité avec des résultats. Utilisé dans ResultatInstance.
 *  Il peut y avoir plusieurs CommoditeResultat pour une seule Commodite. 
 *  Un CommoditeResultat représente le résultat obtenu pour la commodité que l'on étend avec une méthode de résolution.
 */
public class CommoditeResultat extends Commodite {

	//Liste des arcs utilisés par cette commodité.
	public ArrayList<Arc> arcsUtilises;
	//Longueur de cette comodité.
	public double length;
	
	/**
	 * Construit une commodité.
	 * @param dep Sommet de départ.
	 * @param arr Sommet d'arrivée.
	 */
	public CommoditeResultat(int dep, int arr) {
		//appel au constructeur de Commodite
		super(dep, arr);
		this.arcsUtilises = new ArrayList<Arc>();
	}
	
	/**
	 * Ajout d'un arc utilisé par cette commodité.
	 * @param arc Arc à ajouter.
	 */
	public void addArc(Arc arc) {
		this.arcsUtilises.add(arc);	
	}
	
	/**
	 * Permet de mettre les arcs utilisés dans l'ordre du départ vers l'arrivée 
	 * Les arcs que l'on récupère via Julia sont les bons mais il ne sont pas forcément écrits dans le bon ordre d'utilisation ni avec (depart,arrivée).
	 * Par exemple une commodité de 1 vers 2 peut donner comme résultat (6,5) (1,5) (2,6) et il faut remettre dans l'ordre pour obtenir (1,5) (5,6) (6,2).
	 */
	public void ordonnerArcs() {
		//Copie de la liste des arcs
		ArrayList<Arc> tempo = (ArrayList<Arc>) arcsUtilises.clone();
		arcsUtilises.clear();
		int sommet = arrivee;
		Arc selected = null;
		/*
		 * Pour remettre les arcs dans l'ordre on construit la liste d'arcs à l'envers. On sait qu'il n'y a pas de cycles dans le graphe.
		 * On sait donc qu'on ne passe qu'une seule fois par l'arrivée. On commence par récupérer l'arc contenant l'arrivée
		 * et on place l'arrivée comme arrivee de l'arc (c'est ce qui est fait dans la méthode contains).
		 * Ensuite on refait cela sauf que le sommet recherché devient le sommet de départ de l'arc que l'on vient de sélectionner.
		 * L'arc que l'on trouve est placé en tête de liste des arcs utilisés.
		 * Et on fait cela jusqu'à arriver au sommet de départ de l'arc, c'est à dire quand la liste temporaire est vide.
		 * Au final on obtient une liste des arcs utilisés avec tous les arcs dans le bon ordre et les départs/arrivées aussi.
		 */
		while (!tempo.isEmpty()) {
			//parcours de tous les arcs
			for(int i=0; i<tempo.size(); i++) {
				Arc a = tempo.get(i);
				if (a.contains(sommet)){
					selected = a;
				}
			}
			//ajoute l'arc sélectionné au début de la liste des arcs et le supprime de la liste des arcs à traiter.
			this.arcsUtilises.add(0, selected);
			tempo.remove(selected);
			sommet = selected.origine;
		}
	}

}
