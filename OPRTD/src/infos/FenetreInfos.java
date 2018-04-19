package infos;

import javax.swing.JSplitPane;

import affichageCentral.Instance;

/**
 * Conteneur contenant toutes les informations sur la droite de la fen�tre principale.
 */
public class FenetreInfos extends JSplitPane {
	
	//Fen�tre contenant les boutons pour afficher ou cacher des �l�ments sur la carte.
	FenetreInfosHaut haut;
	//Fen�tre affichant les infos sur les commodit�s
	FenetreInfosBas bas;
	//Fen�tre permettant d'afficher les r�sultats de 2 m�thodes pour comparer
	FenetreInfosComparaison comparaison;
	
	/**
	 * Cr�e toute la partie info sur la droite de la fen�tre principale.
	 * @param i L'instance qui est en cours de traitement.
	 */
	public FenetreInfos(Instance i) {
		/* La fen�tre est s�par�e en 2 en hauteur */
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		//On ne peut mettre que 2 �l�ments dans un SplitPane donc on imbrique des SplitPane les uns dans les autres pour avoir plus de 2 �l�ments.
		JSplitPane secondSplit = new JSplitPane();
		secondSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);		
		this.haut = new FenetreInfosHaut(i, this);		
		secondSplit.add(this.haut, JSplitPane.TOP);
		this.bas = new FenetreInfosBas();		
		secondSplit.add(this.bas, JSplitPane.BOTTOM);
		//On ajoute le premier SplitPane au conteneur principal et on ajoute la 3�me fen�tre
		add(secondSplit, JSplitPane.TOP);
		this.comparaison = new FenetreInfosComparaison(i);
		add(this.comparaison, JSplitPane.BOTTOM);
	}
	
	/**
	 * Getter de la fenetre bas (celle contenant les infos sur les commodit�s).
	 * @return La fenetre affichant les informations sur les commodit�s.
	 */
	public FenetreInfosBas getBas() {
		return this.bas;
	}
	
	/**
	 * Getter de la fen�tre haut contenant les controles pour afficher des informations sur le graphe.
	 * @return La fenetre haut.
	 */
	public FenetreInfosHaut getHaut() {
		return this.haut;
	}
	
	/**
	 * Getter de la fen�tre contenant les informations de comparaison des m�thodes.
	 * @return La fen�tre de comparaison.
	 */
	public FenetreInfosComparaison getComparaison() {
		return this.comparaison;
	}
	
	/**
	 * Permet de rendre les contr�les utilisables une fois que les r�sultats ont �t� obtenus.
	 */
	public void activer() {
		//Active dans la fen�tre haute et dans la fen�tre de comparaison.
		this.haut.activer();
		this.comparaison.activer();
	}
	
	/**
	 * Permet de d�sactiver les controles pour les rendre inutilisables. Utile au lancement de l'application
	 * pour emp�cher d'utiliser des fonctionnalit�s avant d'avoir obtenu des r�sultats de r�solution.
	 */
	public void desactiver() {
		//D�sctive dans la fen�tre haute et dans la fen�tre de comparaison.
		this.haut.desactiver();
		this.comparaison.desactiver();
	}
}