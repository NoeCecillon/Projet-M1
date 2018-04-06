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
 * Classe représentant une instance avec les arcs, les noeuds, les commodités et les résultats.
 */
public class Instance extends mxGraph {
	
	// Tous les noeuds présents dans l'instance. Indéxés par leur ID
	HashMap<Integer, Noeud> noeuds;
	// Liste de tous les arcs dans l'instance.
	ArrayList<Arc> arcs;
	// Tous les résultats de cette instance indéxés par le nom de la méthode de résolution utilisée pour obtenir ces résultats.
	HashMap<String, ResultatInstance> resultats;
	// Toutes les commodités qu'il y a dans l'instance indéxées par leur ID (l'ID est récupéré dans la sortie de Julia : 0,1,2,...)
	HashMap<Integer, Commodite> commodites;
	// Booléens pour savoir ce qui est affichée en ce moment sur la carte.
	private boolean reseauBaseAffiche = true;
	private boolean sousReseauAffiche = false;
	private int commoditeAffichee = 0;
	// Chemin d'accès vers le dossier contenant l'instance
	private String path;
	/* Fenetre principale de l'application */
	private FenetrePrincipale fenetrePrincipale;
	/* Coeddicient de zoom. 1 = zoom de base*/
	private int zoom = 1;
	/* Nom de la méthode qui est sélectionnée */
	private String selectedMethod;
	/* Référence vers la fenêtre contenant les informations affichées sur les commodités */
	private FenetreInfos fenetreInfosCommodity;
	
	/**
	 * Constructeur permettant de créer une instance réelle ou random.
	 * @param reelle Permet d'indiquer si on veut créer une instance réelle ou non. true = réelle, false = random.
	 * @param path Chemin vers le dossier contenant l'instance.
	 */
	public Instance(boolean reelle, String path) {
		this.resultats = new HashMap<String, ResultatInstance>();
		this.path = path;
	/* Si le paramètre est true, on crée une instance réelle sinon une instance aléatoire */	
		if (reelle) {
			creationInstanceReelle();
		} else {
			creationInstanceRandom();
		}
        /* Met en place la taille des noeuds et des arcs en fonction de la densité du graphe */
		adapterTailleGraphe();
	}
	
	/**
	 * Constructeur d'une instance réelle.
	 */
	public void creationInstanceReelle() {
		
		/* Contient les noeuds insérés */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs insérés */
		this.arcs = new ArrayList<Arc>();
		
		// On commence à modifier le modèle mxGraph
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			// Ouvre le fichier contenant tous les arcs
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        // Lecture du fichier ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	//supprime tous les espaces dans une ligne et sépare split la ligne à chaque virgule
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		// si ce n'est pas un chiffre le parse lève une NumberFormatException exception
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		// si aucune exception n'a été levée c'est qu'on est sur une ligne contenant un arc
	        		//si le noeud départ n'existe pas dans notre modèle on le crée et l'ajoute
	        		if (!noeuds.containsKey(depart)) {
	        			//la position (ici 50,50) n'a pas d'importance car on va la modifier juste après 
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		//si le noeud arrivée n'existe pas dans notre modèle on le crée et l'ajoute
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		// Maintenant qu'on a créé les noeuds on ajoute l'arc entre ces 2 noeuds.
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        	} catch (NumberFormatException e) {}	        	
	        }
	        br.close();
	        // Fin de lecture du fichier. Tous les noeuds et arcs ont été ajoutés dans le modèle.
	        
	        // Maintenant on passe au placement des noeuds.
	        /* Lecture du fichier position pour récupérer les positions max/min */
	        fichier = new File(path+System.getProperty("file.separator")+"position.csv");
	        br = new BufferedReader(new FileReader(fichier));
	        double xMin = 0;
	        double xMax = 0;
	        double yMin = 0;
	        double yMax = 0;
	        //lecture ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	
	        	/* On peut faire des parseDouble uniquement avec des 9.2 pas 9,2 */
	        	// on doit donc remplacer les , par des . mais seulement les , dans des nombres, pas celles séparant 2 colonnes.
	        	line = line.replaceAll(",\\s", "&&&");
	        	line = line.replaceAll(",", ".");
	        	line = line.replaceAll("&&&", ", ");
	        	String[] ligne = line.split(", ");
	        	double x = Double.parseDouble(ligne[1]);
	        	double y = Double.parseDouble(ligne[2]);
	        	// vérifie si les valeurs sont > à une valeur max ou < à une valeur min.
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
	         * On recommence une lecture. Cette fois on va réellement placer les noeuds.
	         * Pour calculer leurs positions, on utilise les val min et max que l'on a récupérées juste avant.
	         * Le noeud avec la valeur xMax sera tout à droite de la fenêtre et celui avec xMin sera tout à gauche.
	         * Les autres noeuds sont placés proportionnellement. Par ex si xMin = 100 et xMax = 300, un noeud avec x=200 sera au milieu de la fenêtre
	         * un noeud avec x=250 sera à 75% de la largeur.
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
	        	// Récupère l'objet noeud correspondant.
	        	Noeud n = noeuds.get(Integer.parseInt(ligne[0]));
	        	
	        	double valeur = 0;
	        	
	        	//Calcul des pourcentages et modification des valeurs dans les objets noeuds
	        	if (x>0) {
	        		valeur = (x+Math.abs(xMin))/(xMax-xMin);
	        	} else {
	        		valeur = 1-((Math.abs(x)/(xMax-xMin)));
	        	}
	        	//On s'assure que les noeuds ne sont pas collés au bord pour qu'on puisse avoir un affichage correct.
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
		// Tous les arcs et noeuds sont créés et les noeuds ont les valeurs en pourcentage de là ou ils doivent être placés. (ils ne sont pas encore placés au bon endroit sur la map mais ça va se faire dès le prochain redimensionnement de la carte)
		
		// Maintenant on ajoute les commodités dans l'instance
		this.commodites = new HashMap<Integer, Commodite>();
		// Les commodités sont dans le fichier 3
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        //numéro de la commodité
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			// Lecture ligne par ligne
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
				// enlève les espaces et split avec , comme séparateur
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
		// A la fin de cette méthode, toute l'instance a été construite. Arcs, sommets et commodités ont été ajoutés dans les variables.
		// Les arcs et les sommets ont également été ajoutés dans le modèle mxGraph pour être affichés.
	}
	
	/**
	 * Constructeur d'une instance réelle.
	 * Similaire à la méthode creationInstanceReelle() sauf pour le positionnement des noeuds.
	 */
	public void creationInstanceRandom() {
		
		/* Contient les noeuds insérés */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs insérés */
		this.arcs = new ArrayList<Arc>();
		
		// Début de la modification du modèle mxGraph
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        
	     // Lecture du fichier ligne par ligne
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	//supprime tous les espaces dans une ligne et sépare split la ligne à chaque virgule
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		// si ce n'est pas un chiffre le parse lève une NumberFormatException exception
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		// si aucune exception n'a été levée c'est qu'on est sur une ligne contenant un arc
	        		//si le noeud départ n'existe pas dans notre modèle on le crée et l'ajoute
	        		if (!noeuds.containsKey(depart)) {
	        			//la position (ici 50,50) n'a pas d'importance car on va la modifier juste après 
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		//si le noeud arrivée n'existe pas dans notre modèle on le crée et l'ajoute
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		// Maintenant qu'on a créé les noeuds on ajoute l'arc entre ces 2 noeuds.
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        	} catch (NumberFormatException e) {}	        	
	        }
	        br.close();
	        // Fin de lecture du fichier. Tous les noeuds et arcs ont été ajoutés dans le modèle.
			
			/* Dans une instance random on ne récupère pas la position des noeuds dans un fichier mais on utilise
	         * un layout automatique qui place les noeuds.
	         */
	    	mxGraphLayout m = new mxFastOrganicLayout(this);	    	
	    	m.execute(parent);
	    	// Tous les noeuds ont été placés par le layout automatique.
	    	
	    	/*
	    	 * On va maintenant récupérer les positions des noeuds pour les stocker et pouvoir les utiliser
	    	 * lors du redimensionnement de la fenêtre. On commence par rechercher les valeurs min et max sur chaque axe.
	    	 */
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	double xMin = 0;
	    	double xMax = 0;
	    	double yMin = 0;
	    	double yMax = 0;
	    	
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();
	    		// récupère les coordonnées du noeud. (on récupère directement dans l'objet mxCell)
	    		// ces coordonnées sont celles utilisées par le modèle pour afficher le graphe
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
	    	// Une fois qu'on a les valeurs min/max on calcule les positions en pourcentage comme expliqué dans la méthode creationInstanceReelle()
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
	    	while(itr2.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	  
	    		double x = entry.getValue().node.getGeometry().getX();
	    		double y = entry.getValue().node.getGeometry().getY();
	    		//Récupère les valeurs et les met dans les variables.
	    		if (x/(xMax-xMin) < 0.03) {
	    			entry.getValue().x = 0.03;
	    		} else if (x/(xMax-xMin) > 0.95) {
	    			entry.getValue().x = 0.95;
	    		} else {
	    			entry.getValue().x = x/(xMax-xMin);
	    		}
	    		//On s'assure que les noeuds ne sont pas collés au bord pour qu'on puisse avoir un affichage correct.
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
		 * A la fin de cette méthode, les noeuds sont placés sur le graphe et on a stocké leur position pour
		 * pouvoir gérer le redimensionnement de la fenêtre.
		 */
		
		// Maintenant on va ajouter les commodités en lisant dans le fichier 3
		this.commodites = new HashMap<Integer, Commodite>();
		// Les commodités sont dans le fichier 3
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        //numéro de la commodité
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			// Lecture ligne par ligne
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
				// enlève les espaces et split avec , comme séparateur
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
		// A la fin de cette méthode, toute l'instance a été construite. Arcs, sommets et commodités ont été ajoutés dans les variables.
		// Les arcs et les sommets ont également été ajoutés dans le modèle mxGraph pour être affichés.
	}
	
	/**
	 * Méthode lançant l'éxécution du modèle Julia pour récupérer les résultats.
	 * @param fenetrePrincipale Fenetre principale de l'application.
	 * @param cheminMethode Chemin vers la méthode à utiliser pour résoudre l'instance.
	 */
	public void calculSolution(FenetrePrincipale fenetrePrincipale, String cheminMethode) {
		//si ce n'est pas la première résolution, on remet l'affichage comme il est au lancement de l'application avant de calculer
		if (this.selectedMethod != null) {
			//affiche uniquement le réseau de base
			if (this.fenetreInfosCommodity!=null) {
				afficheCommodity(0, this.fenetreInfosCommodity);
				//remet les controles comme il faut (réseau base activé, sous réseau désactivé et commodité sur 0)
				this.fenetreInfosCommodity.getHaut().resetControls();
			}
			afficherReseauBase();
			cacherReseauSelectionne();
		}
		//on stocke le nom du modèle sélectionné en enlevant le .jl
		this.selectedMethod = new File(cheminMethode).getName().replace(".jl", "");
		this.fenetrePrincipale = fenetrePrincipale;
		/* Julia est éxécuté dans un thread séparé */
		ThreadJulia t = new ThreadJulia(path, this, cheminMethode);
		t.start();
		/* Affichage de la progressbar de chargement */
		//doit forcément être après le start du thread
		this.fenetrePrincipale.chargement();
	}
	
	/**
	 * Lit les résultats dans le texte fourni en sortie par Julia.
	 * @param result Texte de sortie de Julia (ce qui est affiché dans la console quand Julia est éxécuté en mode console)
	 */
	public void lectureResultats(String result) {
		//on crée un nouveau résultat ou on remplace celui qui était dans la map si il y avait déjà un résultat pour cette méthode de résolution.
		resultats.put(selectedMethod, new ResultatInstance());
		
		// On sépare la String en un tableau ou chaque case contient une ligne de la string result.
		String []lignes = result.split(System.getProperty("line.separator"));
		int numCommodity = 0;
		
		for(int i=0; i<lignes.length; i++) {
			
			// Récupère le sous réseau
			if (lignes[i].startsWith("subgraph ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				// On récupère tous les arcs qui sont écrits
				for (int j=2; j<mots.length; j++) {
					//supprime les parenthèses
					String s = mots[j].replaceAll("[()]", "");
					String[] sommets = s.split(",");
					/* On recherche l'arc correspondant dans le modèle et on l'ajoute à la liste du sous réseau */
					for(int k=0; k<arcs.size(); k++) {
						Arc a = arcs.get(k);
						// si l'arc écrit dans le texte correspond à cet objet, on le rajoute dans le sous réseau sélectionné
						if((a.origine == Integer.parseInt(sommets[0]) && a.destination == Integer.parseInt(sommets[1])) || (a.origine == Integer.parseInt(sommets[1]) && a.destination == Integer.parseInt(sommets[0]))) {
							//rajoute l'arc à la liste d'arcs sélectionnés du résultat
							this.resultats.get(this.selectedMethod).ajouterSousReseau(a);
						}
					}
				}
			}
			
			// Récupère les commodités 
			if (lignes[i].startsWith("commodity ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				//récupère le num de la commodité
				numCommodity = Integer.parseInt(mots[1]);
				if (this.commodites.get(numCommodity) != null) {
					/* On crée une nouvelle commoditeResultat */
					this.resultats.get(selectedMethod).ajouterCommodite(numCommodity, new CommoditeResultat(this.commodites.get(numCommodity).depart, this.commodites.get(numCommodity).arrivee));
					/* On récupère tous les arcs qui sont écrits */
					for (int j=3; j<mots.length; j++) {
						String s = mots[j].replaceAll("[()]", "");
						String[] sommets = s.split(",");
						/* On récupère la commoditeResultat qu'on vient de créer et on y ajoute l'arc */
						this.resultats.get(selectedMethod).commodites.get(numCommodity).addArc(new Arc(Integer.parseInt(sommets[0]),Integer.parseInt(sommets[1])));
					}	
				}
			}
			/* Récupère la longueur des commodités */
			if (lignes[i].startsWith("length : ")) {
				String mot = lignes[i].replace("length : ", ""); 
				//rajoute la longueur de la commodité
				this.resultats.get(selectedMethod).commodites.get(numCommodity).length = Double.parseDouble(mot);
			}
		}
		/* On ordonne les arcs dans toutes les commodités */
		Iterator<HashMap.Entry<Integer, CommoditeResultat>> iterateur = this.resultats.get(selectedMethod).commodites.entrySet().iterator();
    	while(iterateur.hasNext()) {
    		HashMap.Entry<Integer, CommoditeResultat> entry = iterateur.next();	  
    		entry.getValue().ordonnerArcs();
    	}
    	/* Une fois que tous les résultats sont récupérés on active les boutons de la fenetre infos */
		this.fenetrePrincipale.getFenetreInfos().activer();
		/* On enlève la progressbar à la fin du chargement */
		this.fenetrePrincipale.finChargement();
		/* Affichage de la sortie de julia dans la console */
		FenetreConsole.getFenetreConsole().printSimplex(result);
		
		// A la fin de cette méthode, on peut afficher le résultat obtenu pour cette instance et la méthode de résolution choisie.
	}

	/**
	 * Replace les noeuds quand la fenêtre est redimensionnée.
	 * @param width Largeur de la fenêtre contenant le graphe (la fenêtre au centre).
	 * @param height Hauteur de la fenêtre contenant le graphe (la fenêtre au centre).
	 */
	public void replacerNoeuds(int width, int height) {
		/*
		 * Pour redimensionner la fenêtre on a le pourcentage en x et en y de chaque point donc on multiplie juste ce pourcentage
		 * par la nouvelle largeur/hauteur de la fenêtre pour savoir on le noeud doit être placé.
		 * Le coefficient de zoom permet d'agrandir la carte pour zoomer dedans. La carte deviens alors plus grande 
		 * que le cadre et on peut se déplacer dedans avec les scrollbars.
		 */
		/* Parcours de tous les noeuds */
		Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
    	while(itr2.hasNext()) {
    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	      		
    		/* modifie les coordonnées des noeuds */
    		mxGeometry geo = (mxGeometry) getModel().getGeometry(entry.getValue().node).clone();
    		/* -30 pour ne pas être trop collé en bas. Multiplis par le coefficient de zoom pour agrandir ou rétrécir la carte */
    		geo.setX(entry.getValue().x * (width*zoom));
    		geo.setY(entry.getValue().y * ((height-30))*zoom);
    		getModel().setGeometry(entry.getValue().node, geo);
    	}
	}
	
	/**
	 * Affiche le réseau de base du graphe en pointillés.
	 */
	public void afficherReseauBase() {
		this.reseauBaseAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* Vérifie comment l'affichage doit être modifié en fonction des autres choix d'affichage */
			if((!sousReseauAffiche || !resultats.get(selectedMethod).sousReseauContient(arc)) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du réseau de base */
				arc.arc.setVisible(true);
				// style de l'arc (pas de flèche, trait large et pointillés
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
				refresh();
			}
		}
	}
	
	/**
	 * Cache le réseau de base du graphe.
	 */
	public void cacherReseauBase() {
		this.reseauBaseAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* Vérifie comment l'affichage doit être modifié en fonction des autres choix d'affichage */
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
	 * Affiche le sous réseau sélectionné pour la solution sélectionnée.
	 */
	public void afficherReseauSelectionne() {
		this.sousReseauAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* Vérifie comment l'affichage doit être modifié */
			if(resultats.get(selectedMethod).sousReseauContient(arc) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du sous réseau */
				arc.arc.setVisible(true);
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=0;");
				refresh();
			}
		}
	}
	
	/**
	 * Cache le sous réseau sélectionné pour la solution sélectionnée.
	 */
	public void cacherReseauSelectionne() {
		this.sousReseauAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* Vérifie comment l'affichage doit être modifié */
			if(resultats.get(selectedMethod).sousReseauContient(arc) && (commoditeAffichee == 0 || !this.resultats.get(selectedMethod).commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit cacher l'arc */
				arc.arc.setVisible(false);
				refresh();
				/* Pour afficher correctement le réseau de base */
				if(reseauBaseAffiche) {
					afficherReseauBase();
				}
			}
		}
	}
	
	/**
	 * Affiche une commodité sur le graphe. 
	 * Afficher la commodité 0 permet d'effacer toutes les commodités qui étaient affichées.
	 * @param idComm ID de la commodité à afficher.
	 * @param conteneur Fenetre contenant les infos sur la droite de la fenêtre principale de l'application.
	 */
	public void afficheCommodity(int idComm, FenetreInfos conteneur) {
		//afficher la commodité 0 permet d'effacer celle qui est affichée sans en afficher une nouvelle.
		/* On supprime les infos affichées */
		conteneur.getBas().vider();
		conteneur.getBas().repaint();
		
		/* Si une commodité est déjà affichée on la supprime avant d'afficher l'autre */
		if (commoditeAffichee != 0) {
			CommoditeResultat c = this.resultats.get(selectedMethod).commodites.get(commoditeAffichee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				Arc arc = c.arcsUtilises.get(i);
				/* On supprime l'arc qui était affiché */
				getModel().remove(arc.arc);
			}
		}
		commoditeAffichee = idComm;
		/* Si idComm vaut 0 c'est qu'on ne doit pas afficher de commodité */
		if(commoditeAffichee != 0) {		
			CommoditeResultat c = this.resultats.get(selectedMethod).commodites.get(idComm);
			FenetreConsole.getFenetreConsole().printConsole("Commodity "+idComm+" : "+c.depart+" -> "+c.arrivee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				/* On crée un nouvel arc et on l'ajoute au modèle */
				Arc arc = c.arcsUtilises.get(i);
				//récupère les noeuds départ et arrivée
				mxCell depart =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.origine);
				mxCell arrivee =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.destination);
				//On ajoute un arc par dessus celui existant. 
				Object a = insertEdge(getDefaultParent(), null, null, depart, arrivee);
				mxCell arcCree = (mxCell) a;
				arc.arc = arcCree;
				// Le nouvel arc que l'on ajoute est rouge et est orienté
				arcCree.setStyle("strokeColor=#ce103c;strokeWidth=2;dashed=0;");
				refresh();	
			}
			
			/* Affichage des infos sur la commodité sélectionnée dans la fenetre infos à droite */
			this.fenetreInfosCommodity = conteneur;
			conteneur.getBas().vider();
			conteneur.getBas().ajouter(new JLabel("Commodity "+idComm+ " : " + c.depart + "->" +c.arrivee));
			conteneur.getBas().ajouter(new JLabel("Length : "+c.length));
			conteneur.getBas().revalidate();
		}
	}
	
	/**
	 * Getter du chemin vers la méthode.
	 * @return Le chemin vers la méthode de résolution séectionnée.
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * Méthode appelée après la création de l'affichage permettant d'adapter le style à la densité du graphe.
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
		/* Vérifie que la variable est bien initialisée pour éviter NullPointerException. 
		 * Il ne faut pas appeler cette fonction avant d'avoir solve l'instance sinon la variable n'est pas initialisée. */
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
	 * Fonction pour dézoomer l'affichage de la carte.
	 */
	public void dezoomer() {
		/* Vérifie que la variable est bien initialisée pour éviter NullPointerException. 
		 * Il ne faut pas appeler cette fonction avant d'avoir solve l'instance sinon la variable n'est pas initialisée. */
		if (this.fenetrePrincipale != null) {
			// le zoom est de 1 au minimum
			if (this.zoom >1) {
				this.zoom --;
				replacerNoeuds(fenetrePrincipale.getFenetreCentre().getWidth(), fenetrePrincipale.getFenetreCentre().getHeight());
			}
		}
	}
	
	/**
	 * Permet de modifier le résultat qui est affiché. 
	 * Prend en paramètre le nom de la méthode dont les résultats doivent être affichés.
	 * @param methode La méthode de résolution que l'on veut sélectionner.
	 */
	public void setMethodeSelectionnee(String methode) {
		// Quand on change la méthode de résolution, on remet l'affichage de base.
		if (this.selectedMethod != null) {
			//affiche uniquement le réseau de base
			afficherReseauBase();
			cacherReseauSelectionne();
			if (this.fenetreInfosCommodity!=null) {
				afficheCommodity(0, this.fenetreInfosCommodity);
				//remet les controles comme il faut (réseau base activé, sous réseau désactivé et commodité sur 0)
				this.fenetreInfosCommodity.getHaut().resetControls();
			}
		}
		this.selectedMethod = methode;
	}
	
	/**
	 * Getter de la liste des commodités.
	 * @return une Map contenant toutes les commodités de l'instance indéxées par leur ID.
	 */
	public HashMap<Integer, Commodite> getCommodites() {
		return this.commodites;
	}
	
}
