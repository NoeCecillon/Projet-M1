package infos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import affichageCentral.Instance;


public class FenetreInfosHaut extends JPanel {

	Instance instance;
	
	private JCheckBox cbReseau;
	private JCheckBox cbSelect;
	private JComboBox choixCommodite;
	private FenetreInfos parent;
	private JButton zoomIn;
	private JButton zoomOut;
	private JComboBox choixMethode;
	private JRadioButton affichageClassique;
	private JRadioButton affichageReduit;
	
	public Box box;
	
	public FenetreInfosHaut(Instance ins, FenetreInfos parent) {
		this.instance = ins;
		this.parent = parent;
		
		/* Conteneur qui permet de mettre les éléments les uns au dessus des autres */
		this.box = Box.createVerticalBox();
		
		/* Ajout des boutons pour zoomer sur le graphe */
		JLabel textZoom = new JLabel("Zoom carte ");
		this.zoomIn = new JButton("+");
		this.zoomOut = new JButton("-");
		// on met les 3 éléments dans une HBox pour qu'ils soient sur une seule ligne
		Box Hbox = Box.createHorizontalBox();
		Hbox.add(textZoom);
		Hbox.add(zoomIn);
		//event clic sur le bouton zoom
		zoomIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				instance.zoomer();
			}
		});
		//event clic sur le bouton dezoom
		Hbox.add(zoomOut);
		zoomOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				instance.dezoomer();
			}
		});
		box.add(Hbox);
		
		/* Ajout des radio buttons pour sélectionner le mode d'affichage des noeuds */
		// On met les boutons dans un groupe pour qu'un seul bouton puisse être sélectionné à la fois
		ButtonGroup group = new ButtonGroup();
		affichageClassique = new JRadioButton("Classique");
		affichageReduit = new JRadioButton("Réduit");
		group.add(affichageClassique);
		group.add(affichageReduit);
		//De base c'est l'affichage classique qui est sélectionné
		affichageClassique.setSelected(true);
		JLabel textChoixAffichage = new JLabel("Mode d'affichage ");
		// on met les 3 éléments dans une HBox pour qu'ils soient sur une seule ligne
		Box HboxAffNoeuds = Box.createHorizontalBox();
		HboxAffNoeuds.add(textChoixAffichage);
		HboxAffNoeuds.add(affichageClassique);
		HboxAffNoeuds.add(affichageReduit);
		
		//Event clic sur affichage reduit
		affichageReduit.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//Si on sélectionne affichage réduit 
			    if (e.getStateChange() == ItemEvent.SELECTED) {
			    	instance.setAffichageReduit();
			    }
			    //Si on déselctionne l'affichage réduit (donc on sélectionne l'affichage classique)
			    else if (e.getStateChange() == ItemEvent.DESELECTED) {
			        instance.setAffichageClassique();
			    }
			}
		});
		// Ajout dans le conteneur principal
		box.add(HboxAffNoeuds);
		
		/* Crée la combobox permettant de choisir les résultats de quelle méthode on veut afficher. 
		 * */		
		choixMethode = new JComboBox();
		//on récupère le nom des méthodes dans le dossier methods
		String [] fichiers = new File("methods").list(); 
		for (String nomMethode : fichiers){
			String nom = nomMethode.replace(".jl", "");
		}
		//Au clic on modifie la méthode sélectionnée dans l'instance
		choixMethode.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	instance.setMethodeSelectionnee(choixMethode.getSelectedItem().toString());
		    }
		});
		box.add(choixMethode);		
		
		// checkbox pour afficher ou non le réseau de base
		cbReseau = new JCheckBox("Réseau base");
		cbReseau.setSelected(true);
		/* affiche ou cache le réseau de base */
		cbReseau.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if (cbReseau.isSelected()) {
		    		  instance.afficherReseauBase();
		    	  } else {
		    		  instance.cacherReseauBase();
		    	  }
		      }
		});
		
		// checkbox pour afficher ou non le sous réseau sélectionné
		cbSelect = new JCheckBox("Réseau sélectionné");
		/* Affiche ou cache le sous reseau selectionne */
		cbSelect.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if (cbSelect.isSelected()) {
		    		  instance.afficherReseauSelectionne();
		    	  } else {
		    		  instance.cacherReseauSelectionne();
		    	  }
		      }
		});
		
		box.add(cbReseau);
		box.add(cbSelect);
		
		//Permet de sélectionner la commodité à afficher
		choixCommodite = new JComboBox();
		choixCommodite.addItem("-----");
		for (int i=0; i<instance.getCommodites().size(); i++) {
			choixCommodite.addItem("Commodity " + (i+1));
		}
		
		choixCommodite.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (choixCommodite.getSelectedItem().toString().startsWith("Commodity")) {
		    		afficherCommodity(choixCommodite);
		    	}
		    	/* Si on a sélectionné la première case, on efface toutes les commodités */
		    	else {
		    		instance.afficheCommodity(0, parent);
		    	}
		    }
		});
		
		box.add(choixCommodite);
		add(box);
	}
	
	public void afficherCommodity(JComboBox choixCommodite) {
		instance.afficheCommodity(Integer.parseInt(choixCommodite.getSelectedItem().toString().replaceAll("Commodity ", "")), this.parent);
	}
	
	/* Active les controles */
	public void activer() {
		this.cbReseau.setEnabled(true);
		this.cbSelect.setEnabled(true);
		this.choixCommodite.setEnabled(true);
		this.zoomIn.setEnabled(true);
		this.zoomOut.setEnabled(true);
		this.choixMethode.setEnabled(true);
		this.affichageClassique.setEnabled(true);
		this.affichageReduit.setEnabled(true);
	}
	
	/* Désactive les controles */
	public void desactiver() {
		this.cbReseau.setEnabled(false);
		this.cbSelect.setEnabled(false);
		this.choixCommodite.setEnabled(false);
		this.zoomIn.setEnabled(false);
		this.zoomOut.setEnabled(false);
		this.choixMethode.setEnabled(false);
		this.affichageClassique.setEnabled(false);
		this.affichageReduit.setEnabled(false);
	}
	
	/* Remet les boutons comme ils sont à l'ouverture de l'application */
	public void resetControls() {
		this.cbReseau.setSelected(true);
		this.cbSelect.setSelected(false);
		this.choixCommodite.setSelectedIndex(0);
		this.affichageClassique.setSelected(true);
		this.affichageReduit.setSelected(false);
	}
	
	/* Permet d'ajouter un item dans la combobox des méthodes */
	public void addMethode(String methode) {
		this.choixMethode.addItem(methode);
	}
}
