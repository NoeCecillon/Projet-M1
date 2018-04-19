package barreMenu;
import java.awt.Image;
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
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

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
				
		JMenu mnOuvrir = new JMenu("Open");
		menuInstance.add(mnOuvrir);
		
		/* Item permettant d'ouvrir une instance random. Ouvre un navigateur permettant de s�lectionner le dossier contenant l'instance random. */
		JMenuItem itemOpenReal = new JMenuItem("Real");
		itemOpenReal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				JFileChooser choix = new JFileChooser();
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
				JFileChooser choix = new JFileChooser();
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
		String [] fichiers = new File("src"+System.getProperty("file.separator")+"methods").list(); 
		for (String nomMethode : fichiers){
			String nom = nomMethode.replace(".jl", "");
			JMenuItem nouvelItem = new JMenuItem(nom);
			//au clic sur l'item, on lance la r�solution en donnant le chemin vers le fichier de la m�thode en param�tre
			nouvelItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					parent.resoudre("src"+System.getProperty("file.separator")+"methods"+System.getProperty("file.separator")+nomMethode);
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
				//Texte d'aide qui sera affich� dans le fileChooser. String g�n�r�e avec https://codebeautify.org/java-escape-unescape pour transformer un texte normal en un texte utilisable dans une String.
				String texteAide = "Pour ajouter une nouvelle m�thode de r�solution, s�lectionnez le fichier contenant la m�thode.\nLe fichier doit �tre au format .jl et son nom doit �tre celui que vous voulez utiliser comme nom de la m�thode.\nPour pouvoir �tre utilis�, le fichier doit respecter les conditions suivantes :\n\t- En d�but de fichier, juste apr�s les \"using XXX\", il faut ajouter la ligne suivante \"File=string(ARGS[1])\".\n\t- Les chemins vers les fichiers fichier_1.csv, fichier_2.csv et fichier_3.csv doivent �tre remplac�s par \'File*\"fichier_3.csv\"\'.\n\t- Le sous r�seau s�lectionn� doit �tre affich� en commen�ant par \"subgraph :\" puis avec tous les arcs sous la forme  (27,20)   (57,27)   (58,57)   (64,50) ...\n\t- Le r�sultat des commodit�s doit �tre affich� en commen�ant par \"commodity X :\" ou X est le num�ro de la commodit� et suivi des sommets utilis�s par cette commodit� sous la forme commodity 1 :    (77,79)   (79,111)   (80,77) ...\n\t- La ligne suivant doit contenir la longueur de la commodit� sous la forme \"length : 3203.966569\"";
				//Ouvre le s�lecteur de fichier contenant un bouton aide. On donne le texte d'aide au constructeur.
				CustomFileChooser choix = new CustomFileChooser(texteAide);
				int retour = choix.showOpenDialog(parent.getContentPane());
				//Si l'utilisateur a s�lectionn� un fichier
				if(retour==JFileChooser.APPROVE_OPTION){
					//V�rifie que c'est bien un fichier .jl
					if (choix.getSelectedFile().getName().contains(".jl")) {
						//Pour copier le fichier on doit utiliser des Path contenant l'adresse des dossiers source et destination.
						Path src = choix.getSelectedFile().toPath();
						//La destination doit �tre le nouveau chemin vers le fichier
						Path dest = new File("src"+System.getProperty("file.separator")+"methods"+System.getProperty("file.separator")+choix.getSelectedFile().getName()).toPath();
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
			JFileChooser choix = new JFileChooser();
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
