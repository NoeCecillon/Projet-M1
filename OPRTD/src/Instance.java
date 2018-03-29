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

import logs.FenetreConsole;

public class Instance extends mxGraph {
	
	HashMap<Integer, Noeud> noeuds;
	ArrayList<Arc> arcs;
	ArrayList<Arc> sousReseau;
	HashMap<Integer, Commodite> commodites;
	
	private boolean reseauBaseAffiche = true;
	private boolean sousReseauAffiche = false;
	private int commoditeAffichee = 0;
	// Chemin d'acc�s vers le dossier contenant l'instance
	private String path;
	/* Fenetre principale de l'application */
	private FenetrePrincipale fenetrePrincipale;
	/* Coeddicient de zoom. 1 = zoom de base*/
	private int zoom = 1;
	
	public Instance(boolean reelle, String path) {
		
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
	
	public void creationInstanceReelle() {
		
		/* Contient les noeuds ins�r�s */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs ins�r�s */
		this.arcs = new ArrayList<Arc>();
		
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		if (!noeuds.containsKey(depart)) {
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		/* Ajout arcs */
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        	} catch (NumberFormatException e) {}	        	
	      	
	        }
	        br.close();
	        
	        /* Lecture du fichier position pour r�cup�rer les positions max/min */
	        fichier = new File(path+System.getProperty("file.separator")+"position.csv");
	        br = new BufferedReader(new FileReader(fichier));
	        double xMin = 0;
	        double xMax = 0;
	        double yMin = 0;
	        double yMax = 0;
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	
	        	/* On peut faire des parseDouble uniquement avec des 9.2 pas 9,2 */
	        	line = line.replaceAll(",\\s", "&&&");
	        	line = line.replaceAll(",", ".");
	        	line = line.replaceAll("&&&", ", ");
	        	String[] ligne = line.split(", ");
	        	double x = Double.parseDouble(ligne[1]);
	        	double y = Double.parseDouble(ligne[2]);
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

	        /* Calcul des position en pourcentage */
	        br = new BufferedReader(new FileReader(fichier));
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	 
	        	line = line.replaceAll(",\\s", "&&&");
	        	line = line.replaceAll(",", ".");
	        	line = line.replaceAll("&&&", ", ");
	        	String[] ligne = line.split(", ");
	        	double x = Double.parseDouble(ligne[1]);
	        	double y = Double.parseDouble(ligne[2]);
	        	Noeud n = noeuds.get(Integer.parseInt(ligne[0]));
	        	
	        	double valeur = 0;
	        	
	        	if (x>0) {
	        		valeur = (x+Math.abs(xMin))/(xMax-xMin);
	        	} else {
	        		valeur = 1-((Math.abs(x)/(xMax-xMin)));
	        	}
	        	
	        	if (valeur < 0.03) {
	    			n.x = 0.03;
	    		} else if (valeur > 0.95) {
	    			n.x = 0.95;
	    		} else {
	    			n.x = valeur;
	    		}
	    		
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
		
		/* Lecture des commodit�s das le fichier */	
		this.commodites = new HashMap<Integer, Commodite>();
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
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
	}
	
	public void creationInstanceRandom() {
		
		/* Chemin vers le dossier ou les fichiers des instances g�n�r�es */
		//String path = "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\ProjetM1\\generateur";
		/* Contient les noeuds ins�r�s */
		this.noeuds = new HashMap<Integer, Noeud>();
		/* Contient les arcs ins�r�s */
		this.arcs = new ArrayList<Arc>();
		
		Object parent = getDefaultParent(); 
		getModel().beginUpdate();
		try
		{
			File fichier = new File(path+System.getProperty("file.separator")+"fichier_1.csv");
	        BufferedReader br = new BufferedReader(new FileReader(fichier));
	        
	        
	        for (String line = br.readLine(); line != null; line = br.readLine()) {	   
	        	line = line.replaceAll("\\s", "");
	        	String[] ligne = line.split(",");
	        	// Si la ligne commence par un chiffre
	        	try {
	        		int depart = Integer.parseInt(ligne[0]);
	        		int arrivee = Integer.parseInt(ligne[1]);
	        		if (!noeuds.containsKey(depart)) {
	        			Object o = insertVertex(parent, "s"+depart, depart, 50, 50, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(depart,new Noeud(cell));	        			
	        		}
	        		if (!noeuds.containsKey(arrivee)) {
	        			Object o = insertVertex(parent, "s"+arrivee, arrivee, 0, 0, 25, 25, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_ELLIPSE);
	        			mxCell cell = (mxCell) o;
	        			noeuds.put(arrivee, new Noeud(cell));
	        		}
	        		/* Ajout arcs */
	        		Object arc = insertEdge(getDefaultParent(), null, null, noeuds.get(depart).node, noeuds.get(arrivee).node, "endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
	        		//ajout dans la liste
	        		arcs.add(new Arc(depart, arrivee, (mxCell) arc));
	        		
	        	} catch (NumberFormatException e) {}	        	
	      	
	        }
	        br.close();
			
			/* On utilise un layout automatique pour placer les noeuds */
	    	mxGraphLayout m = new mxFastOrganicLayout(this);	    	
	    	m.execute(parent);
	    	
	    	/* Parcours de tous les noeuds pour qu'ils calculent leurs positions en % */
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	double xMin = 0;
	    	double xMax = 0;
	    	double yMin = 0;
	    	double yMax = 0;
	    	
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();	  
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
	    	/* Calcul des position en pourcentage */
	    	Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
	    	while(itr2.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	  
	    		double x = entry.getValue().node.getGeometry().getX();
	    		double y = entry.getValue().node.getGeometry().getY();
	    		
	    		if (x/(xMax-xMin) < 0.03) {
	    			entry.getValue().x = 0.03;
	    		} else if (x/(xMax-xMin) > 0.95) {
	    			entry.getValue().x = 0.95;
	    		} else {
	    			entry.getValue().x = x/(xMax-xMin);
	    		}
	    		
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
		
		/* Lecture des commodit�s das le fichier */
		
		this.commodites = new HashMap<Integer, Commodite>();
		File fichier = new File(path+System.getProperty("file.separator")+"fichier_3.csv");
        BufferedReader br;
        int nb = 0;
		try {
			br = new BufferedReader(new FileReader(fichier));
			for (String line = br.readLine(); line != null; line = br.readLine()) {	 
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
	}
	
	/* M�thode lan�ant l'�x�cution du mod�le Julia pour r�cup�rer les r�sultats */
	public void calculSolution(FenetrePrincipale fenetrePrincipale) {
		this.fenetrePrincipale = fenetrePrincipale;
		/* Julia est �x�cut� dans un thread s�par� */
		ThreadJulia t = new ThreadJulia(path, this);
		t.start();
		/* Affichage de la progressbar de chargement */
		//doit forc�ment �tre apr�s le start du thread
		this.fenetrePrincipale.chargement();
	}
	
	
	/* Lit les r�sultats dans le texte fourni en sortie par Julia */
	public void lectureResultats(String result) {
		String []lignes = result.split(System.getProperty("line.separator"));
		this.sousReseau = new ArrayList<Arc>();
		int numCommodity = 0;
		
		for(int i=0; i<lignes.length; i++) {
			
			/* R�cup�re le sous r�seau */
			if (lignes[i].startsWith("subgraph ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				for (int j=2; j<mots.length; j++) {
					String s = mots[j].replaceAll("[()]", "");
					String[] sommets = s.split(",");
					/* On recherche l'arc et on l'ajoute � la liste du sous r�seau */
					for(int k=0; k<arcs.size(); k++) {
						Arc a = arcs.get(k);
						if((a.origine == Integer.parseInt(sommets[0]) && a.destination == Integer.parseInt(sommets[1])) || (a.origine == Integer.parseInt(sommets[1]) && a.destination == Integer.parseInt(sommets[0]))) {
							this.sousReseau.add(a);
						}
					}
				}
			}
			
			/* R�cup�re les commodit�s */
			if (lignes[i].startsWith("commodity ")) {
				//remplace les espaces multiples par un espace simple
				String l = lignes[i].replaceAll("\\s+", " ");
				String[] mots = l.split(" ");
				//r�cup�re le num de la commodit�
				numCommodity = Integer.parseInt(mots[1]);
				if (this.commodites.get(numCommodity) != null) {
					for (int j=3; j<mots.length; j++) {
						String s = mots[j].replaceAll("[()]", "");
						String[] sommets = s.split(",");
						/* On ajoute les arcs utilis�s dans l'objet commodite */
						this.commodites.get(numCommodity).addArc(new Arc(Integer.parseInt(sommets[0]),Integer.parseInt(sommets[1])));
					}	
				}
			}
			/* R�cup�re la longueur des commodit�s */
			if (lignes[i].startsWith("length : ")) {
				String mot = lignes[i].replace("length : ", ""); 
				this.commodites.get(numCommodity).length = Double.parseDouble(mot);
			}
		}
		/* On ordonne les arcs dans toutes les commodit�s */
		Iterator<HashMap.Entry<Integer, Commodite>> iterateur = commodites.entrySet().iterator();
    	while(iterateur.hasNext()) {
    		HashMap.Entry<Integer, Commodite> entry = iterateur.next();	  
    		entry.getValue().ordonnerArcs();
    	}
    	/* Une fois que tous les r�sultats sont r�cup�r�s on active les boutons de la fenetre infos */
		this.fenetrePrincipale.getFenetreInfos().activer();
		/* On enl�ve la progressbar � la fin du chargement */
		this.fenetrePrincipale.finChargement();
		/* Affichage de la sortie de julia dans la console */
		FenetreConsole.getFenetreConsole().printSimplex(result);
	}

	/* Replace les noeuds quand la fen�tre est redimensionn�e */
	public void replacerNoeuds(int width, int height) {
		/* Parcours de tous les noeuds */
		Iterator<HashMap.Entry<Integer, Noeud>> itr2 = noeuds.entrySet().iterator();
    	while(itr2.hasNext()) {
    		HashMap.Entry<Integer, Noeud> entry = itr2.next();	      		
    		/* modifie les coordonn�es des noeuds */
    		mxGeometry geo = (mxGeometry) getModel().getGeometry(entry.getValue().node).clone();
    		/* -30 pour ne pas �tre trop coll� en bas. Multiplis par le corfficient de zoom pour agrandir ou r�tr�cir la carte */
    		geo.setX(entry.getValue().x * (width*zoom));
    		geo.setY(entry.getValue().y * ((height-30))*zoom);
    		getModel().setGeometry(entry.getValue().node, geo);
    	}
	}
	
	/* Retourne une liste contenant le nom de chaque commodit� (g�n�ralement 1, 2, 3, ...) */
	public ArrayList<String> getCommodName() {
		ArrayList<String> ret = new ArrayList<String>();
		Iterator<HashMap.Entry<Integer, Commodite>> iterateur = commodites.entrySet().iterator();
    	while(iterateur.hasNext()) {
    		HashMap.Entry<Integer, Commodite> entry = iterateur.next();	  
    		ret.add(entry.getKey().toString());
    	}
    	return ret;
	}
	
	/* Affiche le r�seau de base */
	public void afficherReseauBase() {
		this.reseauBaseAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if((!sousReseauAffiche || !sousReseau.contains(arc)) && (commoditeAffichee == 0 || !commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du r�seau de base */
				arc.arc.setVisible(true);
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=1;");
				refresh();
			}
		}
	}
	
	/* Cache le r�seau de base */
	public void cacherReseauBase() {
		this.reseauBaseAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if((!sousReseauAffiche || !sousReseau.contains(arc)) && (commoditeAffichee == 0 || !commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
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
	
	/* Affiche le sous r�seau s�lectionn� */
	public void afficherReseauSelectionne() {
		this.sousReseauAffiche = true;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if(sousReseau.contains(arc) && (commoditeAffichee == 0 || !commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
				/* Dans ce cas on doit afficher l'arc comme faisant partie du sous r�seau */
				arc.arc.setVisible(true);
				arc.arc.setStyle("endArrow=none;strokeColor=#42bc5a;strokeWidth=2;dashed=0;");
				refresh();
			}
		}
	}
	
	/* Cache le sous r�seau s�lectionn� */
	public void cacherReseauSelectionne() {
		this.sousReseauAffiche = false;
		for(int i=0; i<arcs.size(); i++) {
			Arc arc = arcs.get(i);
			/* V�rifie comment l'affichage doit �tre modifi� */
			if(sousReseau.contains(arc) && (commoditeAffichee == 0 || !commodites.get(commoditeAffichee).arcsUtilises.contains(arc))) {
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
	
	public void afficheCommodity(int idComm, FenetreInfos conteneur) {
		
		/* Si une commodit� est d�j� affich�e on la supprime avant d'afficher l'autre */
		if (commoditeAffichee != 0) {
			Commodite c = commodites.get(commoditeAffichee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				Arc arc = c.arcsUtilises.get(i);
				/* On supprime l'arc qui �tait affich� */
				getModel().remove(arc.arc);
			}
		}
		commoditeAffichee = idComm;
		/* Si idComm vaut 0 c'est qu'on ne doit pas afficher de commodit� */
		if(commoditeAffichee != 0) {		
			Commodite c = commodites.get(idComm);
			FenetreConsole.getFenetreConsole().printConsole("Commodity "+idComm+" : "+c.depart+" -> "+c.arrivee);
			for(int i=0; i<c.arcsUtilises.size(); i++) {
				/* On cr�e un nouvel arc et on l'ajoute au mod�le */
				Arc arc = c.arcsUtilises.get(i);
				mxCell depart =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.origine);
				mxCell arrivee =(mxCell) ((mxGraphModel) getModel()).getCell("s"+arc.destination);
				Object a = insertEdge(getDefaultParent(), null, null, depart, arrivee);
				mxCell arcCree = (mxCell) a;
				arc.arc = arcCree;
				arcCree.setStyle("strokeColor=#ce103c;strokeWidth=2;dashed=0;");
				refresh();	
			}
			
			/* Affichage des infos sur la commodit� s�lectionn�e */
			conteneur.getBas().vider();
			conteneur.getBas().ajouter(new JLabel("Commodity "+idComm+ " : " + c.depart + "->" +c.arrivee));
			conteneur.getBas().ajouter(new JLabel("Length : "+c.length));
			conteneur.getBas().revalidate();
		}
	}
	
	public String getPath() {
		return this.path;
	}
	
	/* M�thode appel�e apr�s la cr�ation de l'affichage permettant d'adapter le style � la densit� du graphe */
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
	    		getModel().setStyle(entry.getValue().node, mxConstants.STYLE_SHAPE + "="+mxConstants.SHAPE_RECTANGLE);
	    		entry.getValue().node.getGeometry().setWidth(15);
	    		entry.getValue().node.getGeometry().setHeight(15);
	    	}
		}
		else if (noeuds.size() > 65) {
			Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();	  
	    		entry.getValue().node.getGeometry().setWidth(15);
	    		entry.getValue().node.getGeometry().setHeight(15);
	    	}
		}
		else if (noeuds.size() > 30) {
			Iterator<HashMap.Entry<Integer, Noeud>> itr = noeuds.entrySet().iterator();
	    	while(itr.hasNext()) {
	    		HashMap.Entry<Integer, Noeud> entry = itr.next();	  
	    		entry.getValue().node.getGeometry().setWidth(20);
	    		entry.getValue().node.getGeometry().setHeight(20);
	    	}
		}
	}
	
	/* Fonction pour zoomer */
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
	
	/* Fonction pour d�zoomer */
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
}
