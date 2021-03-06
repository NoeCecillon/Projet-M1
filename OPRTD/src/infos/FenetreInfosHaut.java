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
		
		/* Conteneur qui permet de mettre les �l�ments les uns au dessus des autres */
		this.box = Box.createVerticalBox();
		
		/* Ajout des boutons pour zoomer sur le graphe */
		JLabel textZoom = new JLabel("Zoom carte ");
		this.zoomIn = new JButton("+");
		this.zoomOut = new JButton("-");
		// on met les 3 �l�ments dans une HBox pour qu'ils soient sur une seule ligne
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
		
		/* Ajout des radio buttons pour s�lectionner le mode d'affichage des noeuds */
		// On met les boutons dans un groupe pour qu'un seul bouton puisse �tre s�lectionn� � la fois
		ButtonGroup group = new ButtonGroup();
		affichageClassique = new JRadioButton("Classique");
		affichageReduit = new JRadioButton("R�duit");
		group.add(affichageClassique);
		group.add(affichageReduit);
		//De base c'est l'affichage classique qui est s�lectionn�
		affichageClassique.setSelected(true);
		JLabel textChoixAffichage = new JLabel("Mode d'affichage ");
		// on met les 3 �l�ments dans une HBox pour qu'ils soient sur une seule ligne
		Box HboxAffNoeuds = Box.createHorizontalBox();
		HboxAffNoeuds.add(textChoixAffichage);
		HboxAffNoeuds.add(affichageClassique);
		HboxAffNoeuds.add(affichageReduit);
		
		//Event clic sur affichage reduit
		affichageReduit.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//Si on s�lectionne affichage r�duit 
			    if (e.getStateChange() == ItemEvent.SELECTED) {
			    	instance.setAffichageReduit();
			    }
			    //Si on d�selctionne l'affichage r�duit (donc on s�lectionne l'affichage classique)
			    else if (e.getStateChange() == ItemEvent.DESELECTED) {
			        instance.setAffichageClassique();
			    }
			}
		});
		// Ajout dans le conteneur principal
		box.add(HboxAffNoeuds);
		
		/* Cr�e la combobox permettant de choisir les r�sultats de quelle m�thode on veut afficher. 
		 * */		
		choixMethode = new JComboBox();
		//on r�cup�re le nom des m�thodes dans le dossier methods
		String [] fichiers = new File("methods").list(); 
		for (String nomMethode : fichiers){
			String nom = nomMethode.replace(".jl", "");
		}
		//Au clic on modifie la m�thode s�lectionn�e dans l'instance
		choixMethode.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	instance.setMethodeSelectionnee(choixMethode.getSelectedItem().toString());
		    }
		});
		box.add(choixMethode);		
		
		// checkbox pour afficher ou non le r�seau de base
		cbReseau = new JCheckBox("R�seau base");
		cbReseau.setSelected(true);
		/* affiche ou cache le r�seau de base */
		cbReseau.addItemListener(new ItemListener() {
		      public void itemStateChanged(ItemEvent e) {
		    	  if (cbReseau.isSelected()) {
		    		  instance.afficherReseauBase();
		    	  } else {
		    		  instance.cacherReseauBase();
		    	  }
		      }
		});
		
		// checkbox pour afficher ou non le sous r�seau s�lectionn�
		cbSelect = new JCheckBox("R�seau s�lectionn�");
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
		
		//Permet de s�lectionner la commodit� � afficher
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
		    	/* Si on a s�lectionn� la premi�re case, on efface toutes les commodit�s */
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
	
	/* D�sactive les controles */
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
	
	/* Remet les boutons comme ils sont � l'ouverture de l'application */
	public void resetControls() {
		this.cbReseau.setSelected(true);
		this.cbSelect.setSelected(false);
		this.choixCommodite.setSelectedIndex(0);
		this.affichageClassique.setSelected(true);
		this.affichageReduit.setSelected(false);
	}
	
	/* Permet d'ajouter un item dans la combobox des m�thodes */
	public void addMethode(String methode) {
		this.choixMethode.addItem(methode);
	}
}
