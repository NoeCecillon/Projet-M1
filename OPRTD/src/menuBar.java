import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class menuBar extends JMenuBar {

	FenetrePrincipale parent;
	
	public menuBar(FenetrePrincipale parent) {
		this.parent = parent;
		JMenu menuInstance = new JMenu("Instance");
		add(menuInstance);
		JMenu menuMethode = new JMenu("Method");
		add(menuMethode);
		JMenu menuSolution = new JMenu("Solution");
		add(menuSolution);
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
		
		/* Bouton pour lancer la r�solution */
		JMenuItem itemSolve = new JMenuItem("Solve");
		itemSolve.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				parent.resoudre();
			}
		});
		menuInstance.add(itemSolve);
		
		
		
		JRadioButtonMenuItem rdbtnmntmMthode = new JRadioButtonMenuItem("Method 1");
		menuMethode.add(rdbtnmntmMthode);
		
		JRadioButtonMenuItem rdbtnmntmMthode_1 = new JRadioButtonMenuItem("Method 2");
		rdbtnmntmMthode_1.setSelected(true);
		menuMethode.add(rdbtnmntmMthode_1);
		
		JRadioButtonMenuItem rdbtnmntmMthode_2 = new JRadioButtonMenuItem("Method 3");
		menuMethode.add(rdbtnmntmMthode_2);
		
		JMenu mnComparatif = new JMenu("Comparatif");
		menuMethode.add(mnComparatif);
		
		JCheckBoxMenuItem chckbxmntmMthode = new JCheckBoxMenuItem("Method 1");
		chckbxmntmMthode.setSelected(true);
		mnComparatif.add(chckbxmntmMthode);
		
		JCheckBoxMenuItem chckbxmntmMthode_1 = new JCheckBoxMenuItem("Method 2");
		mnComparatif.add(chckbxmntmMthode_1);
		
		JCheckBoxMenuItem chckbxmntmMthode_2 = new JCheckBoxMenuItem("Method 3");
		chckbxmntmMthode_2.setSelected(true);
		mnComparatif.add(chckbxmntmMthode_2);
		
		JMenuItem mntmAjouter = new JMenuItem("Ajouter");
		menuMethode.add(mntmAjouter);
		
		
		
		
		JMenuItem mntmStatistiques = new JMenuItem("Statistiques");
		menuSolution.add(mntmStatistiques);
		
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
