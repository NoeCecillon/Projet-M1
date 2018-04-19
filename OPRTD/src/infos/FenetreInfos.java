package infos;

import javax.swing.JSplitPane;

import affichageCentral.Instance;

/**
 * Conteneur contenant toutes les informations sur la droite de la fenêtre principale.
 */
public class FenetreInfos extends JSplitPane {
	
	//Fenêtre contenant les boutons pour afficher ou cacher des éléments sur la carte.
	FenetreInfosHaut haut;
	//Fenêtre affichant les infos sur les commodités
	FenetreInfosBas bas;
	//Fenêtre permettant d'afficher les résultats de 2 méthodes pour comparer
	FenetreInfosComparaison comparaison;
	
	/**
	 * Crée toute la partie info sur la droite de la fenêtre principale.
	 * @param i L'instance qui est en cours de traitement.
	 */
	public FenetreInfos(Instance i) {
		/* La fenêtre est séparée en 2 en hauteur */
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		//On ne peut mettre que 2 éléments dans un SplitPane donc on imbrique des SplitPane les uns dans les autres pour avoir plus de 2 éléments.
		JSplitPane secondSplit = new JSplitPane();
		secondSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);		
		this.haut = new FenetreInfosHaut(i, this);		
		secondSplit.add(this.haut, JSplitPane.TOP);
		this.bas = new FenetreInfosBas();		
		secondSplit.add(this.bas, JSplitPane.BOTTOM);
		//On ajoute le premier SplitPane au conteneur principal et on ajoute la 3ème fenêtre
		add(secondSplit, JSplitPane.TOP);
		this.comparaison = new FenetreInfosComparaison(i);
		add(this.comparaison, JSplitPane.BOTTOM);
	}
	
	/**
	 * Getter de la fenetre bas (celle contenant les infos sur les commodités).
	 * @return La fenetre affichant les informations sur les commodités.
	 */
	public FenetreInfosBas getBas() {
		return this.bas;
	}
	
	/**
	 * Getter de la fenêtre haut contenant les controles pour afficher des informations sur le graphe.
	 * @return La fenetre haut.
	 */
	public FenetreInfosHaut getHaut() {
		return this.haut;
	}
	
	/**
	 * Getter de la fenêtre contenant les informations de comparaison des méthodes.
	 * @return La fenêtre de comparaison.
	 */
	public FenetreInfosComparaison getComparaison() {
		return this.comparaison;
	}
	
	/**
	 * Permet de rendre les contrôles utilisables une fois que les résultats ont été obtenus.
	 */
	public void activer() {
		//Active dans la fenêtre haute et dans la fenêtre de comparaison.
		this.haut.activer();
		this.comparaison.activer();
	}
	
	/**
	 * Permet de désactiver les controles pour les rendre inutilisables. Utile au lancement de l'application
	 * pour empêcher d'utiliser des fonctionnalités avant d'avoir obtenu des résultats de résolution.
	 */
	public void desactiver() {
		//Désctive dans la fenêtre haute et dans la fenêtre de comparaison.
		this.haut.desactiver();
		this.comparaison.desactiver();
	}
}