package barreMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import aide.FenetreAide;
import main.FenetrePrincipale;

public class menuBar extends JMenuBar {

	FenetrePrincipale parent;
	
	public menuBar(FenetrePrincipale parent) {
		this.parent = parent;
		JMenu menuInstance = new JMenu("Instance");
		add(menuInstance);
		JMenu menuMethode = new JMenu("Method");
		add(menuMethode);
		JMenu menuAide = new JMenu("Help");
		add(menuAide);
		// Ajout de l'action pour le clic sur l'onglet "aide"
		JMenuItem itemAide = new JMenuItem("Aide");
		itemAide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {	
				//Ouvre le fen�tre d'aide
				new FenetreAide();
			}
		});
		menuAide.add(itemAide);
			
		JMenu mnOuvrir = new JMenu("Open");
		menuInstance.add(mnOuvrir);
		
		/* Item permettant d'ouvrir une instance r�elle. Ouvre un navigateur permettant de s�lectionner le dossier contenant l'instance r�elle. */
		JMenuItem itemOpenReal = new JMenuItem("Real");
		itemOpenReal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				CustomFileChooser choix = new CustomFileChooser("ouvertureInstance");
				choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				/* On s�lectionne un dossier et pas un fichier */
				int retour=choix.showOpenDialog(parent.getContentPane());
				if(retour==JFileChooser.APPROVE_OPTION){
				   // chemin absolu du dossier choisi
				   String path = choix.getSelectedFile().getAbsolutePath();
				   File file1 = new File(path+ System.getProperty("file.separator") + "fichier_1.csv");
				   File file2 = new File(path+ System.getProperty("file.separator") + "fichier_2.csv");
				   File file3 = new File(path+ System.getProperty("file.separator") + "fichier_3.csv");
				   File file4 = new File(path+ System.getProperty("file.separator") + "position.csv");
				   /* Si un ou plusieurs fichiers n'existent pas dans le r�pertoire s�lectionn� on affiche un message d'erreur */
				   if(!file1.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_1.csv introuvable dans " + path +".");
				   }
				   if(!file2.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_2.csv introuvable dans " + path +".");
				   }
				   if(!file3.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_3.csv introuvable dans " + path +".");
				   }
				   if(!file4.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : position.csv introuvable dans " + path +".");
				   }
				   
				   /* Si il y a tous les fichiers n�cessaires on charge l'instance, sinon on affiche une erreur */
				   if(file1.isFile() && file2.isFile() && file3.isFile() && file4.isFile()) {
					   /* Le true permet d'indiquer que c'est une instance r�elle */
					   parent.previsualiserInstance(path, true);
					   parent.getFenetreLog().printConsole("Instance ouverte.");
				   } else {
					   parent.getFenetreLog().printConsole("L'instance n'a pas pu �tre ouverte.");
				   }
				   
				}
			}
		});
		mnOuvrir.add(itemOpenReal);
		
		/* Item permettant d'ouvrir une instance random. Ouvre un navigateur permettant de s�lectionner le dossier contenant l'instance random. */
		JMenuItem itemOpenRandom = new JMenuItem("Random");
		itemOpenRandom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				CustomFileChooser choix = new CustomFileChooser("ouvertureInstance");
				choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				/* On s�lectionne un dossier et pas un fichier */
				int retour=choix.showOpenDialog(parent.getContentPane());
				if(retour==JFileChooser.APPROVE_OPTION){
				   // chemin absolu du dossier choisi
				   String path = choix.getSelectedFile().getAbsolutePath();
				   File file1 = new File(path+ System.getProperty("file.separator") + "fichier_1.csv");
				   File file2 = new File(path+ System.getProperty("file.separator") + "fichier_2.csv");
				   File file3 = new File(path+ System.getProperty("file.separator") + "fichier_3.csv");
				   /* Si un ou plusieurs fichiers n'existent pas dans le r�pertoire s�lectionn� on affiche un message d'erreur */
				   if(!file1.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_1.csv introuvable dans " + path +".");
				   }
				   if(!file2.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_2.csv introuvable dans " + path +".");
				   }
				   if(!file3.isFile()) {
					   parent.getFenetreLog().printConsole("Erreur : fichier_3.csv introuvable dans " + path +".");
				   }
				   
				   /* Si il y a tous les fichiers n�cessaires on charge l'instance, sinon on affiche une erreur */
				   if(file1.isFile() && file2.isFile() && file3.isFile()) {
					   /* Le false permet d'indiquer que c'est une instance random */
					   parent.previsualiserInstance(path, false);
					   parent.getFenetreLog().printConsole("Instance ouverte.");
				   } else {
					   parent.getFenetreLog().printConsole("L'instance n'a pas pu �tre ouverte.");
				   }
				   
				}
			}
		});
		mnOuvrir.add(itemOpenRandom);
		
		/* Bouton pour ouvrir la fenetre de g�n�ration */
		JMenuItem itemGeneration = new JMenuItem("Create");
		itemGeneration.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				parent.createFenetreGeneration();
			}
		});
		menuInstance.add(itemGeneration);
		
		/* Item permettant d'exporter l'instance ouverte dans un fichier zip */
		JMenuItem itemSave = new JMenuItem("Save");
		itemSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				sauverInstance();
			}
		});
		menuInstance.add(itemSave);
		
		
		/* Cr�e les items pour les m�thodes dans le menu correspondant.
		 * On r�cup�re le nom de chaque fichier m�thode dans le dossier methods et on l'affiche comme texte de l'item.
		 */	
		//R�cup�re le chemin vers le package methods contenant les m�thodes
		String [] fichiers = new File("methods").list(); 
		for (String nomMethode : fichiers){
			String nom = nomMethode.replace(".jl", "");
			JMenuItem nouvelItem = new JMenuItem(nom);
			//au clic sur l'item, on lance la r�solution en donnant le chemin vers le fichier de la m�thode en param�tre
			nouvelItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					parent.resoudre("methods"+System.getProperty("file.separator")+nomMethode);
					// Ajoute la m�thode dans les listes sur les fenetres infos � droite
					String nomM = nom;
					parent.getFenetreInfos().getHaut().addMethode(nomM);
					parent.getFenetreInfos().getComparaison().addMethode(nomM);
				}
			});
			menuMethode.add(nouvelItem);
		}
		
		// Item permettant d'ajouter une nouvelle m�thode de r�solution.
		JMenuItem ajouterMethode = new JMenuItem("Ajouter");
		
		ajouterMethode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
		
				//Ouvre le s�lecteur de fichier contenant un bouton aide. On donne le nom de l'ancre vers laquelle on veut rediriger l'utilisateur si il demande de l'aide.
				CustomFileChooser choix = new CustomFileChooser("ajoutMethode");
				int retour = choix.showOpenDialog(null);
				//Si l'utilisateur a s�lectionn� un fichier
				if(retour==JFileChooser.APPROVE_OPTION){
					//V�rifie que c'est bien un fichier .jl
					if (choix.getSelectedFile().getName().contains(".jl")) {
						//Pour copier le fichier on doit utiliser des Path contenant l'adresse des dossiers source et destination.
						Path src = choix.getSelectedFile().toPath();
						//La destination doit �tre le nouveau chemin vers le fichier
						Path dest = new File("methods"+System.getProperty("file.separator")+choix.getSelectedFile().getName()).toPath();
						try {
							Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
							//On recr�e la menuBar pour ajouter la nouvelle m�thode de r�solution.
							parent.reconstruireMenuBar();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		menuMethode.add(ajouterMethode);
		
	}
	
	public void sauverInstance() {
		try {
			CustomFileChooser choix = new CustomFileChooser("sauvegardeInstance");
			choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			/* On s�lectionne un dossier et pas un fichier */
			int retour=choix.showOpenDialog(parent.getContentPane());
			if(retour==JFileChooser.APPROVE_OPTION){
			   // chemin absolu du dossier choisi
			   String path = choix.getSelectedFile().getAbsolutePath();
			   
			   ArrayList<File> fichiersAAjouter = new ArrayList<File>();
			   /* Liste des fichiers � mettre dans l'archive */
			   fichiersAAjouter.add(new File(this.parent.getFenetreCentre().getInstance().getPath()+System.getProperty("file.separator")+"fichier_1.csv"));
			   fichiersAAjouter.add(new File(this.parent.getFenetreCentre().getInstance().getPath()+System.getProperty("file.separator")+"fichier_2.csv"));
			   fichiersAAjouter.add(new File(this.parent.getFenetreCentre().getInstance().getPath()+System.getProperty("file.separator")+"fichier_3.csv"));
			   /* On cr�e l'archive */
			   FileOutputStream output = new FileOutputStream(path+System.getProperty("file.separator")+"sauvegarde.zip");
			   ZipOutputStream zipOut = new ZipOutputStream(output);
			   for (File src : fichiersAAjouter) {
				   FileInputStream f = new FileInputStream(src);
				   ZipEntry zip = new ZipEntry(src.getName());
				   zipOut.putNextEntry(zip);
				   
				   byte[] bytes = new byte[1024];
				   int longueur;
				   while((longueur = f.read(bytes)) >= 0) {
					   zipOut.write(bytes,  0, longueur);
				   }
				   f.close();
			   }
			   zipOut.close();
			   output.close();
			}			
		} catch (Exception e){
			e.printStackTrace();
		}			
	}
}
