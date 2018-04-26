package infos;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import affichageCentral.Instance;

/**
 * Fenetre permettant de sélectionner 2 méthodes et de voir leurs résultats pour pouvoir les comparer.
 */
public class FenetreInfosComparaison extends JSplitPane{

	//instance en cours de traitement
	private Instance instance;
	// combobox qui permettent de choisir les méthodes à comparer
	private JComboBox choixGauche;
	private JComboBox choixDroite;
	public Box box;
	//Labels contenant les temps de résolution
	JLabel tempsGauche;
	JLabel tempsDroite;
	//Labels contenant les valeurs optimales
	JLabel optimalGauche;
	JLabel optimalDroite;
	
	/**
	 * Crée la fenêtre de comparaison.
	 * @param i Instance en cours de traitement.
	 */
	public FenetreInfosComparaison(Instance i) {
		
		this.instance = i;
		
		setLayout(new BorderLayout());
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		//Les 2 conteneurs
		JPanel gauche = new JPanel();
		JPanel droite = new JPanel();
		//Listes déroulantes permettant de choisir les méthodes
		this.choixGauche = new JComboBox();
		this.choixDroite = new JComboBox();
		
		//Action combobox gauche
		choixGauche.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//Modifie les valeurs de temps/valeur opti affichées
		    	tempsGauche.setText(Float.toString(instance.resultats.get(choixGauche.getSelectedItem().toString()).tempsResolution));
		    	optimalGauche.setText(instance.resultats.get(choixGauche.getSelectedItem().toString()).valeurObjectif);
		    }
		});
		//Action combobox droite
		choixDroite.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//Affiche les infos
				  tempsDroite.setText(Float.toString(instance.resultats.get(choixDroite.getSelectedItem().toString()).tempsResolution));
				  optimalDroite.setText(instance.resultats.get(choixDroite.getSelectedItem().toString()).valeurObjectif);
			}
		});
		
		//Conteneur de gauche
		Box boxGauche = Box.createVerticalBox();
		boxGauche.add(choixGauche);
		boxGauche.add(new JLabel("Temps d'execution:"));
		tempsGauche = new JLabel();
		optimalGauche = new JLabel();
		boxGauche.add(tempsGauche);
		boxGauche.add(new JLabel("Valeur optimale:"));
		boxGauche.add(optimalGauche);
		
		//Conteneur de droite
		Box boxDroite = Box.createVerticalBox();
		boxDroite.add(choixDroite);
		boxDroite.add(new JLabel("Temps d'execution:"));
		tempsDroite = new JLabel();
		optimalDroite = new JLabel();
		boxDroite.add(tempsDroite);
		boxDroite.add(new JLabel("Valeur optimale:"));
		boxDroite.add(optimalDroite);
		
		gauche.add(boxGauche);
		droite.add(boxDroite);
		add(gauche, BorderLayout.WEST);
		add(droite, BorderLayout.EAST);
	}
	
	/**
	 * Ajout d'une méthode disponible dans les listes des méthodes à comparer.
	 * @param methode Nom de la méthode à ajouter.
	 */
	public void addMethode(String methode) {
		this.choixDroite.addItem(methode);
		this.choixGauche.addItem(methode);
	}
	
	/**
	 * Rend cliquables les combobox pour afficher une comparaison.
	 */
	public void activer() {
		this.choixDroite.setEnabled(true);
		this.choixGauche.setEnabled(true);
	}
	
	/**
	 * Grise les combobox et les rend inutilisables.
	 */
	public void desactiver() {
		this.choixDroite.setEnabled(false);
		this.choixGauche.setEnabled(false);
	}
}
