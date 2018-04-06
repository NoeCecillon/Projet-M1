package affichageCentral;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JLabel;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import infos.FenetreInfos;
import logs.FenetreConsole;
import main.FenetrePrincipale;

/**
 * Classe repr�sentant une instance avec les arcs, les noeuds, les commodit�s et les r�sultats.
 */
public class Instance extends mxGraph {
	
	// Tous les noeuds pr�sents dans l'instance. Ind�x�s par leur ID
	HashMap<Integer, Noeud> noeuds;
	// Liste de tous les arcs dans l'instance.
	ArrayList<Arc> arcs;
	// Tous les r�sultats de cette instance ind�x�s par le nom de la m�thode de r�solution utilis�e pour obtenir ces r�sultats.
	HashMap<String, ResultatInstance> resultats;
	// Toutes les commodit�s qu'il y a dans l'instance ind�x�es par leur ID (l'ID est r�cup�r� dans la sortie de Julia : 0,1,2,...)
	HashMap<Integer, Commodite> commodites;
	// Bool�ens pour savoir ce qui est affich�e en ce moment sur la carte.
	private boolean reseauBaseAffiche = true;
	private boolean sousReseauAffiche = false;
	private int commoditeAffichee = 0;
	// Chemin d'acc�s vers le dossier contenant l'instance
	private String path;
	/* Fenetre principale de l'application */
	private FenetrePrincipale fenetrePrincipale;
	/* Coeddicient de zoom. 1 = zoom de base*/
	private int zoom = 1;
	/* Nom de la m�thode qui est s�lectionn�e */
	private String selectedMethod;
	/* R�f�rence vers la fen�tre contenant les informations affich�es sur les commodit�s */
	private FenetreInfos fenetreInfosCommodity;
	
	/**
	 * Constructeur permettant de cr�er une instance r�elle ou random.
	 * @param reelle Permet d'indiquer si on veut cr�er une instance r�elle ou non. true = r�elle, false = random.
	 * @param path Chemin vers le dossier contenant l'instance.
	 */
	public Instance(boolean reelle, String path) {
		this.resultats = new HashMap<String, ResultatInstance>();
		this.path = path;
	/* Si le param�tre est true, on cr�e une instance r�elle sinon une instance al�atoire */	
		if (reelle) {
			creationInstanceReelle();
		} else {
			creationInstanceRandom();
		}
        /* Met en place la taille des noeuds et des arcs en fonction de la densit� du graphe */
		adapterTailleGraphe();
	}
	
	/**
	 * Constructeur d'une instance r�elle.
	 */
	public void creationInstanceReelle() {
		
		/* Contient les noeuds ins�r�s */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs ins�r�s */
		this.arcs = new ArrayList<Arc>();
		
		// On commence � modifier le mod�le mxGraph
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			// Ouvre le fichier contenant tous les arcs
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        // Lecture du fichier ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	//supprime tous les espaces dans une ligne et s�pare split la ligne � chaque virgule
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		// si ce n'est pas un chiffre le parse l�ve une NumberFormatException exception
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		// si aucune exception n'a �t� lev�e c'est qu'on est sur une ligne contenant un arc
	        		//si le noeud d�part n'existe pas dans notre mod�le on le cr�e et l'ajoute
	        		if (!noeuds.containsKey(depart)) {
	        			//la position (ici 50,50) n'a pas d'importance car on va la modifier juste apr�s 
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		//si le noeud arriv�e n'existe pas dans notre mod�le on le cr�e et l'ajoute
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		// Maintenant qu'on a cr�� les noeuds on ajoute l'arc entre ces 2 noeuds.
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        	} catch (NumberFormatException e) {}	        	
	        }
	        br.close();
	        // Fin de lecture du fichier. Tous les noeuds et arcs ont �t� ajout�s dans le mod�le.
	        
	        // Maintenant on passe au placement des noeuds.
	        /* Lecture du fichier position pour r�cup�rer les positions max/min */
	        fichier = new File(path+System.getProperty("file.separator")+"position.csv");
	        br = new BufferedReader(new FileReader(fichier));
	        double xMin = 0;
	        double xMax = 0;
	        double yMin = 0;
	        double yMax = 0;
	        //lecture ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	
	        	/* On peut faire des parseDouble uniquement avec des 9.2 pas 9,2 */
	        	// on doit donc remplacer les , par des . mais seulement les , dans des nombres, pas celles s�parant 2 colonnes.
	        	line = line.replaceAll(",\\s", "&&&");
	        	line = line.replaceAll(",", ".");
	        	line = line.replaceAll("&&&", ", ");
	        	String[] ligne = line.split(", ");
	        	double x = Double.parseDouble(ligne[1]);
	        	double y = Double.parseDouble(ligne[2]);
	        	// v�rifie si les valeurs sont > � une valeur max ou < � une valeur min.
	        	if (x > xMax) {
	    			xMax = x;
	    		} else if(x < xMin) {
	    			xMin = x;
	    		}
	    		if (y > yMax) {
	    			yMax = y;
	    		} else if(y < yMin) {
	    			yMin = y;
	    		}
	        }
	        br.close();

	        /*
	         * On recommence une lecture. Cette fois on va r�ellement placer les noeuds.
	         * Pour calculer leurs positions, on utilise les val min et max que l'on a r�cup�r�es juste avant.
	         * Le noeud avec la valeur xMax sera tout � droite de la fen�tre et celui avec xMin sera tout � gauche.
	         * Les autres noeuds sont plac�s proportionnellement. Par ex si xMin = 100 et xMax = 300, un noeud avec x=200 sera au milieu de la fen�tre
	         * un noeud avec x=250 sera � 75% de la largeur.
	         * Pareil pour l'axe y. 
	         */
	        /* Calcul des position en pourcentage */
	        br = new BufferedReader(new FileReader(fichier));
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	 
	        	line = line.replaceAll(",\\s", "&&&");
	        	line = line.replaceAll(",", ".");
	        	line = line.replaceAll("&&&", ", ");
	        	String[] ligne = line.split(", ");
	        	double x = Double.parseDouble(ligne[1]);
	        	double y = Double.parseDouble(ligne[2]);
	        	// R�cup�re l'objet noeud correspondant.
	        	Noeud n = noeuds.get(Integer.parseInt(ligne[0]));
	        	
	        	double valeur = 0;
	        	
	        	//Calcul des pourcentages et modification des valeurs dans les objets noeuds
	        	if (x>0) {
	        		valeur = (x+Math.abs(xMin))/(xMax-xMin);
	        	} else {
	        		valeur = 1-((Math.abs(x)/(xMax-xMin)));
	        	}
	        	//On s'assure que les noeuds ne sont pas coll�s au bord pour qu'on puisse avoir un affichage correct.
	        	if (valeur < 0.03) {
	    			n.x = 0.03;
	    		} else if (valeur > 0.95) {
	    			n.x = 0.95;
	    		} else {
	    			n.x = valeur;
	    		}
	    		
	        	// Pareil avec l'autre axe
	        	if (x>0) {
	        		valeur = (y+Math.abs(yMin))/(yMax-yMin);
	        	} else {
	        		valeur = 1-((Math.abs(y)/(yMax-yMin)));
	        	}
	        	
	    		if (valeur < 0.05) {
	    			n.y = 0.95;
	    		} else if (valeur > 0.97) {
	    			n.y = 0.03;
	    		} else {
	    			n.y = 1-valeur;
	    		}
	        }
	        br.close();

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			getModel().endUpdate();
		}
		// Tous les arcs et noeuds sont cr��s et les noeuds ont les valeurs en pourcentage de l� ou ils doivent �tre plac�s. (ils ne sont pas encore plac�s au bon endroit sur la map mais �a va se faire d�s le prochain redimensionnement de la carte)
		
		// Maintenant on ajoute les commodit�s dans l'instance
		this.commodites = new HashMap<Integer, Commodite>();
		// Les commodit�s sont dans le fichier 3
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        //num�ro de la commodit�
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			// Lecture ligne par ligne
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
				// enl�ve les espaces et split avec , comme s�parateur
				line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	try {
	        		//Si la ligne n'est pas du texte
	        		float dep = Float.parseFloat(ligne[1]);
	        		float arr = Float.parseFloat(ligne[2]);
	        		int depart = (int) dep;
	        		int arrivee = (int) arr;
	        		nb++;
	        		// ajout dans la map
	        		this.commodites.put(nb, new Commodite(depart, arrivee));
	        		
	        	} catch (NumberFormatException e) {}
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// A la fin de cette m�thode, toute l'instance a �t� construite. Arcs, sommets et commodit�s ont �t� ajout�s dans les variables.
		// Les arcs et les sommets ont �galement �t� ajout�s dans le mod�le mxGraph pour �tre affich�s.
	}
	
	/**
	 * Constructeur d'une instance r�elle.
	 * Similaire � la m�thode creationInstanceReelle() sauf pour le positionnement des noeuds.
	 */
	public void creationInstanceRandom() {
		
		/* Contient les noeuds ins�r�s */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs ins�r�s */
		this.arcs = new ArrayList<Arc>();
		
		// D�but de la modification du mod�le mxGraph
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        
	     // Lecture du fichier ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	//supprime tous les espaces dans une ligne et s�pare split la ligne � chaque virgule
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		// si ce n'est pas un chiffre le parse l�ve une NumberFormatException exception
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		// si aucune exception n'a �t� lev�e c'est qu'on est sur une ligne contenant un arc
	        		//si le noeud d�part n'existe pas dans notre mod�le on le cr�e et l'ajoute
	        		if (!noeuds.containsKey(depart)) {
	        			//la position (ici 50,50) n'a pas d'importance car on va la modifier juste apr�s 
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		//si le noeud arriv�e n'existe pas dans notre mod�le on le cr�e et l'ajoute
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		// Maintenant qu'on a cr�� les noeuds on ajoute l'arc entre ces 2 noeuds.
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        	} catch (NumberFormatException e) {}	        	
	        }
	        br.close();
	        // Fin de lecture du fichier. Tous les noeuds et arcs ont �t� ajout�s dans le mod�le.
			
			/* Dans une instance random on ne r�cup�re pas la position des noeuds dans un fichier mais on utilise
	         * un layout automatique qui place les noeuds.
	         */
	    	mxGraphLayout m = new mxFastOrganicLayout(this);	    	
	    	m.execute(parent);
	    	// Tous les noeuds ont �t� plac�s par le layout automatique.
	    	
	    	/*
	    	 * On va maintenant r�cup�rer les positions des noeuds pour les stocker et pouvoir les utiliser
	    	 * lors du redimensionnement de la fen�tre. On commence par rechercher les valeurs min et max sur chaque axe.
	    	 */
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	double xMin = 0;
	    	double xMax = 0;
	    	double yMin = 0;
	    	double yMax = 0;
	    	
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();
	    		// r�cup�re les coordonn�es du noeud. (on r�cup�re directement dans l'objet mxCell)
	    		// ces coordonn�es sont celles utilis�es par le mod�le pour afficher le graphe
	    		double x = entry.getValue().node.getGeometry().getX();
	    		double y = entry.getValue().node.getGeometry().getY();
	    		/* Recherche des positions max/min des noeuds */
	    		if (x > xMax) {
	    			xMax = x;
	    		} else if(x < xMin) {
	    			xMin = x;
	    		}
	    		if (y > yMax) {
	    			yMax = y;
	    		} else if(y < yMin) {
	    			yMin = y;
	    		}
	    	}
	    	// Une fois qu'on a les valeurs min/max on calcule les positions en pourcentage comme expliqu� dans la m�thode creationInstanceReelle()
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
	    	while(itr2.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	  
	    		double x = entry.getValue().node.getGeometry().getX();
	    		double y = entry.getValue().node.getGeometry().getY();
	    		//R�cup�re les valeurs et les met dans les variables.
	    		if (x/(xMax-xMin) < 0.03) {
	    			entry.getValue().x = 0.03;
	    		} else if (x/(xMax-xMin) > 0.95) {
	    			entry.getValue().x = 0.95;
	    		} else {
	    			entry.getValue().x = x/(xMax-xMin);
	    		}
	    		//On s'assure que les noeuds ne sont pas coll�s au bord pour qu'on puisse avoir un affichage correct.
	    		if (y/(yMax-yMin) < 0.03) {
	    			entry.getValue().y = 0.03;
	    		} else if (y/(yMax-yMin) > 0.95) {
	    			entry.getValue().y = 0.95;
	    		} else {
	    			entry.getValue().y = y/(yMax-yMin);
	    		}
	    	}
	    	
	    } catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			getModel().endUpdate();
		}
		/*
		 * A la fin de cette m�thode, les noeuds sont plac�s sur le graphe et on a stock� leur position pour
		 * pouvoir g�rer le redimensionnement de la fen�tre.
		 */
		
		// Maintenant on va ajouter les commodit�s en lisant dans le fichier 3
		this.commodites = new HashMap<Integer, Commodite>();
		// Les commodit�s sont dans le fichier 3
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        //num�ro de la commodit�
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			// Lecture ligne par ligne
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
				// enl�ve les espaces et split avec , comme s�parateur
				line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	try {
	        		//Si la ligne n'est pas du texte
	        		float dep = Float.parseFloat(ligne[1]);
	        		float arr = Float.parseFloat(ligne[2]);
	        		int depart = (int) dep;
	        		int arrivee = (int) arr;
	        		nb++;
	        		// ajout dans la map
	        		this.commodites.put(nb, new Commodite(depart, arrivee));
	        		
	        	} catch (NumberFormatException e) {}
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// A la fin de cette m�thode, toute l'instance a �t� construite. Arcs, sommets et commodit�s ont �t� ajout�s dans les variables.
		// Les arcs et les sommets ont �galement �t� ajout�s dans le mod�le mxGraph pour �tre affich�s.
	}
	
	/**
	 * M�thode lan�ant l'�x�cution du mod�le Julia pour r�cup�rer les r�sultats.
	 * @param fenetrePrincipale Fenetre principale de l'application.
	 * @param cheminMethode Chemin vers la m�thode � utiliser pour r�soudre l'instance.
	 */
	public void calculSolution(FenetrePrincipale fenetrePrincipale, String cheminMethode) {
		//si ce n'est pas la premi�re r�solution, on remet l'affichage comme il est au lancement de l'application avant de calculer
		if (this.selectedMethod != null) {
			//affiche uniquement le r�seau de base
			if (this.fenetreInfosCommodity!=null) {
				afficheCommodity(0, this.fenetreInfosCommodity);
				//remet les controles comme il faut (r�seau base activ�, sous r�seau d�sactiv� et commodit� sur 0)
				this.fenetreInfosCommodity.getHaut().resetControls();
			}
			afficherReseauBase();
			cacherReseauSelectionne();
		}
		//on stocke le nom du mod�le s�lectionn� en enlevant le .jl
		this.selectedMethod = new File(cheminMethode).getName().replace(".jl", "");
		this.fenetrePrincipale = fenetrePrincipale;
		/* Julia est �x�cut� dans un thread s�par� */
		ThreadJulia t = new ThreadJulia(path, this, cheminMethode);
		t.start();
		/* Affichage de la progressbar de chargement */
		//doit forc�ment �tre apr�s le start du thread
		this.fenetrePrincipale.chargement();
	}
	
	/**
	 * Lit les r�sultats dans le texte fourni en sortie par Julia.
	 * @param result Texte de sortie de Julia (ce qui est affich� dans la console quand Julia est �x�cut� en mode console)
	 */
	public void lectureResultats(String result) {
		//on cr�e un nouveau r�sultat ou on remplace celui qui �tait dans la map si il y avait d�j� un r�sultat pour cette m�thode de r�solution.
		resultats.put(selectedMethod, new ResultatInstance());
		
		// On s�pare la String en un tableau ou chaque case contient une ligne de la string result.
		String []lignes = result.split(System.getProperty("line.separator"));
		int numCommodity = 0;
		
		for(int i=0; i<lignes.length; i++) {
			
			// R�cup�re le sous r�seau
			if (lignes[i].startsWith("subgraph ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				// On r�cup�re tous les arcs qui sont �crits
				for (int j=2; j<mots.length; j++) {
					//supprime les parenth�ses
					String s = mots[j].replaceAll("[()]", "");
					String[] sommets = s.split(",");
					/* On recherche l'arc correspondant dans le mod�le et on l'ajoute � la liste du sous r�seau */
					for(int k=0; k<arcs.size(); k++) {
						Arc a = arcs.get(k);
						// si l'arc �crit dans le texte correspond � cet objet, on le rajoute dans le sous r�seau s�lectionn�
						if((a.origine == Integer.parseInt(sommets[0]) && a.destination == Integer.parseInt(sommets[1])) || (a.origine == Integer.parseInt(sommets[1]) && a.destination == Integer.parseInt(sommets[0]))) {
							//rajoute l'arc � la liste d'arcs s�lectionn�s du r�sultat
							this.resultats.get(this.selectedMethod).ajouterSousReseau(a);
						}
					}
				}
			}
			
			// R�cup�re les commodit�s 
			if (lignes[i].startsWith("commodity ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				//r�cup�re le num de la commodit�
				numCommodity = Integer.parseInt(mots[1]);
				if (this.commodites.get(numCommodity) != null) {
					/* On cr�e une nouvelle commoditeResultat */
					this.resultats.get(selectedMethod).ajouterCommodite(numCommodity, new CommoditeResultat(this.commodites.get(numCommodity).depart, this.commodites.get(numCommodity).arrivee));
					/* On r�cup�re tous les arcs qui sont �crits */
					for (int j=3; j<mots.length; j++) {
						String s = mots[j].replaceAll("[()]", "");
						String[] sommets = s.split(",");
						/* On r�cup�re la commoditeResultat qu'on vient de cr�er et on y ajoute l'arc */
						this.resultats.get(selectedMethod).commodites.get(numCommodity).addArc(new Arc(Integer.parseInt(sommets[0]),Integer.parseInt(sommets[1])));
					}	
				}
			}
			/* R�cup�re la longueur des commodit�s */
			if (lignes[i].startsWith("length : ")) {
				String mot = lignes[i].replace("length : ", ""); 
				//rajoute la longueur de la commodit�
				this.resultats.get(selectedMethod).commodites.get(numCommodity).length = Double.parseDouble(mot);
			}
		}
		/* On ordonne les arcs dans toutes les commodit�s */
		Iterator<HashMap.Entry<Integer, CommoditeResultat>> iterateur = this.resultats.get(selectedMethod).commodites.entrySet().iterator();
    	while(iterateur.hasNext()) {
    		HashMap.Entry<Integer, CommoditeResultat> entry = iterateur.next();	  
    		entry.getValue().ordonnerArcs();
    	}
    	/* Une fois que tous les r�sultats sont r�cup�r�s on active les boutons de la fenetre infos */
		this.fenetrePrincipale.getFenetreInfos().activer();
		/* On enl�ve la progressbar � la fin du chargement */
		this.fenetrePrincipale.finChargement();
		/* Affichage de la sortie de julia dans la console */
		FenetreConsole.getFenetreConsole().printSimplex(result);
		
		// A la fin de cette m�thode, on peut afficher le r�sultat obtenu pour cette instance et la m�thode de r�solution choisie.
	}

	/**
	 * Replace les noeuds quand la fen�tre est redimensionn�e.
	 * @param width Largeur de la fen�tre contenant le graphe (la fen�tre au centre).
	 * @param height Hauteur de la fen�tre contenant le graphe (la fen�tre au centre).
	 */
	public void replacerNoeuds(int width, int height) {
		/*
		 * Pour redimensionner la fen�tre on a le pourcentage en x et en y de chaque point donc on multiplie juste ce pourcentage
		 * par la nouvelle largeur/hauteur de la fen�tre pour savoir on le noeud doit �tre plac�.
		 * Le coefficient de zoom permet d'agrandir la carte pour zoomer dedans. La carte deviens alors plus grande 
		 * que le cadre et on peut se d�placer dedans avec les scrollbars.
		 */
		/* Parcours de tous les noeuds */
		Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
    	while(itr2.hasNext()) {
    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	      		
    		/* modifie les coordonn�es des noeuds */
    		mxGeometry geo = (mxGeometry) getModel().getGeometry(entry.getValue().node).clone();
    		/* -30 pour ne pas �tre trop coll� en bas. Multiplis par le coefficient de zoom pour agrandir ou r�tr�cir la carte */
    		geo.setX(entry.getValue().x * (width*zoom));
    		geo.setY(entry.getValue().y * ((height-30))*zoom);
    		getModel().setGeometry(entry.getValue().node, geo);
    	}
	}
	
	/**
	 * Affiche le r�seau de base du graphe en pointill�s.
	 */
	public void afficherReseauBase() {
		this.reseauBaseAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� en fonction des autres choix d'affichage */
			if((!sousReseauAffiche || !resultats.get(selectedMethod).sousReseauContient(arc)) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du r�seau de base */
				arc.arc.setVisible(true);
				// style de l'arc (pas de fl�che, trait large et pointill�s
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
				refresh();
			}
		}
	}
	
	/**
	 * Cache le r�seau de base du graphe.
	 */
	public void cacherReseauBase() {
		this.reseauBaseAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� en fonction des autres choix d'affichage */
			if((!sousReseauAffiche || !resultats.get(selectedMethod).sousReseauContient(arc)) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on cache l'arc */
				arc.arc.setVisible(false);
				refresh();
				/* Pour afficher correctement le sous reseau */
				if(sousReseauAffiche) {
					afficherReseauSelectionne();
				}
			}
		}
	}
	
	/**
	 * Affiche le sous r�seau s�lectionn� pour la solution s�lectionn�e.
	 */
	public void afficherReseauSelectionne() {
		this.sousReseauAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if(resultats.get(selectedMethod).sousReseauContient(arc) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du sous r�seau */
				arc.arc.setVisible(true);
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=0;");
				refresh();
			}
		}
	}
	
	/**
	 * Cache le sous r�seau s�lectionn� pour la solution s�lectionn�e.
	 */
	public void cacherReseauSelectionne() {
		this.sousReseauAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if(resultats.get(selectedMethod).sousReseauContient(arc) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit cacher l'arc */
				arc.arc.setVisible(false);
				refresh();
				/* Pour afficher correctement le r�seau de base */
				if(reseauBaseAffiche) {
					afficherReseauBase();
				}
			}
		}
	}
	
	/**
	 * Affiche une commodit� sur le graphe. 
	 * Afficher la commodit� 0 permet d'effacer toutes les commodit�s qui �taient affich�es.
	 * @param idComm ID de la commodit� � afficher.
	 * @param conteneur Fenetre contenant les infos sur la droite de la fen�tre principale de l'application.
	 */
	public void afficheCommodity(int idComm, FenetreInfos conteneur) {
		//afficher la commodit� 0 permet d'effacer celle qui est affich�e sans en afficher une nouvelle.
		/* On supprime les infos affich�es */
		conteneur.getBas().vider();
		conteneur.getBas().repaint();
		
		/* Si une commodit� est d�j� affich�e on la supprime avant d'afficher l'autre */
		if (commoditeAffichee != 0) {
			CommoditeResultat c = this.resultats.get(selectedMethod).commodites.get(commoditeAffichee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				Arc arc = c.arcsUtilises.get(i);
				/* On supprime l'arc qui �tait affich� */
				getModel().remove(arc.arc);
			}
		}
		commoditeAffichee = idComm;
		/* Si idComm vaut 0 c'est qu'on ne doit pas afficher de commodit� */
		if(commoditeAffichee != 0) {		
			CommoditeResultat c = this.resultats.get(selectedMethod).commodites.get(idComm);
			FenetreConsole.getFenetreConsole().printConsole("Commodity "+idComm+" : "+c.depart+" -> "+c.arrivee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				/* On cr�e un nouvel arc et on l'ajoute au mod�le */
				Arc arc = c.arcsUtilises.get(i);
				//r�cup�re les noeuds d�part et arriv�e
				mxCell depart =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.origine);
				mxCell arrivee =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.destination);
				//On ajoute un arc par dessus celui existant. 
				Object a = insertEdge(getDefaultParent(), null, null, depart, arrivee);
				mxCell arcCree = (mxCell) a;
				arc.arc = arcCree;
				// Le nouvel arc que l'on ajoute est rouge et est orient�
				arcCree.setStyle("strokeColor=#ce103c;strokeWidth=2;dashed=0;");
				refresh();	
			}
			
			/* Affichage des infos sur la commodit� s�lectionn�e dans la fenetre infos � droite */
			this.fenetreInfosCommodity = conteneur;
			conteneur.getBas().vider();
			conteneur.getBas().ajouter(new JLabel("Commodity "+idComm+ " : " + c.depart + "->" +c.arrivee));
			conteneur.getBas().ajouter(new JLabel("Length : "+c.length));
			conteneur.getBas().revalidate();
		}
	}
	
	/**
	 * Getter du chemin vers la m�thode.
	 * @return Le chemin vers la m�thode de r�solution s�ectionn�e.
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * M�thode appel�e apr�s la cr�ation de l'affichage permettant d'adapter le style � la densit� du graphe.
	 */
	public void adapterTailleGraphe() {
		/* Si il y a plus de 15 arcs on passe la width a 1 au lieu de 2 */
		if (arcs.size() > 35) {
			for(int i=0; i<arcs.size(); i++) {
				Arc arc = arcs.get(i);
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=1;dashed=1;");
				refresh();
			}
		}
		/* Modifie la taille des noeuds en fonction du nombre de noeuds */
		if (noeuds.size() > 100) {
			Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();
	    		//Modifie la taille des noeuds pour mieux voir.
	    		//getModel().setStyle(entry.getValue().node, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_RECTANGLE);
	    		entry.getValue().node.getGeometry().setWidth(15);
	    		entry.getValue().node.getGeometry().setHeight(15);
	    	}
		}
		else if (noeuds.size() > 65) {
			Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();
	    		//Modifie la taille des noeuds pour mieux voir.
	    		entry.getValue().node.getGeometry().setWidth(15);
	    		entry.getValue().node.getGeometry().setHeight(15);
	    	}
		}
		else if (noeuds.size() > 30) {
			Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();
	    		//Modifie la taille des noeuds pour mieux voir.
	    		entry.getValue().node.getGeometry().setWidth(20);
	    		entry.getValue().node.getGeometry().setHeight(20);
	    	}
		}
	}
	
	/**
	 * Fonction pour zoomer sur la carte.
	 */
	public void zoomer() {
		/* V�rifie que la variable est bien initialis�e pour �viter NullPointerException. 
		 * Il ne faut pas appeler cette fonction avant d'avoir solve l'instance sinon la variable n'est pas initialis�e. */
		if (this.fenetrePrincipale != null) {
		//le zoom est de 5 au maximum
			if (this.zoom <5) {
				this.zoom ++;
				fenetrePrincipale.getFenetreCentre();
				replacerNoeuds(fenetrePrincipale.getFenetreCentre().getWidth(), fenetrePrincipale.getFenetreCentre().getHeight());
			}	
		}	
	}
	
	/**
	 * Fonction pour d�zoomer l'affichage de la carte.
	 */
	public void dezoomer() {
		/* V�rifie que la variable est bien initialis�e pour �viter NullPointerException. 
		 * Il ne faut pas appeler cette fonction avant d'avoir solve l'instance sinon la variable n'est pas initialis�e. */
		if (this.fenetrePrincipale != null) {
			// le zoom est de 1 au minimum
			if (this.zoom >1) {
				this.zoom --;
				replacerNoeuds(fenetrePrincipale.getFenetreCentre().getWidth(), fenetrePrincipale.getFenetreCentre().getHeight());
			}
		}
	}
	
	/**
	 * Permet de modifier le r�sultat qui est affich�. 
	 * Prend en param�tre le nom de la m�thode dont les r�sultats doivent �tre affich�s.
	 * @param methode La m�thode de r�solution que l'on veut s�lectionner.
	 */
	public void setMethodeSelectionnee(String methode) {
		// Quand on change la m�thode de r�solution, on remet l'affichage de base.
		if (this.selectedMethod != null) {
			//affiche uniquement le r�seau de base
			afficherReseauBase();
			cacherReseauSelectionne();
			if (this.fenetreInfosCommodity!=null) {
				afficheCommodity(0, this.fenetreInfosCommodity);
				//remet les controles comme il faut (r�seau base activ�, sous r�seau d�sactiv� et commodit� sur 0)
				this.fenetreInfosCommodity.getHaut().resetControls();
			}
		}
		this.selectedMethod = methode;
	}
	
	/**
	 * Getter de la liste des commodit�s.
	 * @return une Map contenant toutes les commodit�s de l'instance ind�x�es par leur ID.
	 */
	public HashMap<Integer, Commodite> getCommodites() {
		return this.commodites;
	}
	
}
