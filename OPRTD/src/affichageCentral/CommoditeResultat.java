package affichageCentral;
import java.util.ArrayList;

/**
 *  Repr�sente une commodit� avec des r�sultats. Utilis� dans ResultatInstance.
 *  Il peut y avoir plusieurs CommoditeResultat pour une seule Commodite. 
 *  Un CommoditeResultat repr�sente le r�sultat obtenu pour la commodit� que l'on �tend avec une m�thode de r�solution.
 */
public class CommoditeResultat extends Commodite {

	//Liste des arcs utilis�s par cette commodit�.
	public ArrayList<Arc> arcsUtilises;
	//Longueur de cette comodit�.
	public double length;
	
	/**
	 * Construit une commodit�.
	 * @param dep Sommet de d�part.
	 * @param arr Sommet d'arriv�e.
	 */
	public CommoditeResultat(int dep, int arr) {
		//appel au constructeur de Commodite
		super(dep, arr);
		this.arcsUtilises = new ArrayList<Arc>();
	}
	
	/**
	 * Ajout d'un arc utilis� par cette commodit�.
	 * @param arc Arc � ajouter.
	 */
	public void addArc(Arc arc) {
		this.arcsUtilises.add(arc);	
	}
	
	/**
	 * Permet de mettre les arcs utilis�s dans l'ordre du d�part vers l'arriv�e 
	 * Les arcs que l'on r�cup�re via Julia sont les bons mais il ne sont pas forc�ment �crits dans le bon ordre d'utilisation ni avec (depart,arriv�e).
	 * Par exemple une commodit� de 1 vers 2 peut donner comme r�sultat (6,5) (1,5) (2,6) et il faut remettre dans l'ordre pour obtenir (1,5) (5,6) (6,2).
	 */
	public void ordonnerArcs() {
		//Copie de la liste des arcs
		ArrayList<Arc> tempo = (ArrayList<Arc>) arcsUtilises.clone();
		arcsUtilises.clear();
		int sommet = arrivee;
		Arc selected = null;
		/*
		 * Pour remettre les arcs dans l'ordre on construit la liste d'arcs � l'envers. On sait qu'il n'y a pas de cycles dans le graphe.
		 * On sait donc qu'on ne passe qu'une seule fois par l'arriv�e. On commence par r�cup�rer l'arc contenant l'arriv�e
		 * et on place l'arriv�e comme arrivee de l'arc (c'est ce qui est fait dans la m�thode contains).
		 * Ensuite on refait cela sauf que le sommet recherch� devient le sommet de d�part de l'arc que l'on vient de s�lectionner.
		 * L'arc que l'on trouve est plac� en t�te de liste des arcs utilis�s.
		 * Et on fait cela jusqu'� arriver au sommet de d�part de l'arc, c'est � dire quand la liste temporaire est vide.
		 * Au final on obtient une liste des arcs utilis�s avec tous les arcs dans le bon ordre et les d�parts/arriv�es aussi.
		 */
		while (!tempo.isEmpty()) {
			//parcours de tous les arcs
			for(int i=0; i<tempo.size(); i++) {
				Arc a = tempo.get(i);
				if (a.contains(sommet)){
					selected = a;
				}
			}
			//ajoute l'arc s�lectionn� au d�but de la liste des arcs et le supprime de la liste des arcs � traiter.
			this.arcsUtilises.add(0, selected);
			tempo.remove(selected);
			sommet = selected.origine;
		}
	}

}
