

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FenetreInfosHaut extends JPanel {

	Instance instance;
	
	private JCheckBox cbReseau;
	private JCheckBox cbSelect;
	private JComboBox choixCommodite;
	private FenetreInfos parent;
	private JButton zoomIn;
	private JButton zoomOut;
	
	public Box box;
	
	public FenetreInfosHaut(Instance ins, FenetreInfos parent) {
		this.instance = ins;
		this.parent = parent;
		
		/* Conteneur qui permet de mettre les éléments les uns au dessus des autres */
		this.box = Box.createVerticalBox();
		
		/* Ajout des boutons pour zoomer sur le graphe */
		JLabel textZoom = new JLabel("Zoom");
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
		
		
		choixCommodite = new JComboBox();
		choixCommodite.addItem("-----");
		for (int i=0; i<instance.commodites.size(); i++) {
			choixCommodite.addItem("Commodity " + (i+1));
		}
		
		choixCommodite.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (choixCommodite.getSelectedItem().toString().startsWith("Commodity")) {
		    		afficherCommodity(choixCommodite);
		    	}
		    	/* Si on a sélectionné la première case, on efface toutes les commodités */
		    	else {
		    		instance.afficheCommodity(0, null);
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
	}
	
	/* Désactive les controles */
	public void desactiver() {
		this.cbReseau.setEnabled(false);
		this.cbSelect.setEnabled(false);
		this.choixCommodite.setEnabled(false);
		this.zoomIn.setEnabled(false);
		this.zoomOut.setEnabled(false);
	}
}
