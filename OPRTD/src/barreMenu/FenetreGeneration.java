package barreMenu;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logs.FenetreConsole;
import main.FenetrePrincipale;

/**
 * Fenêtre qui est affichée pour permettre de saisir les valeurs pour générer une instance.
 */
public class FenetreGeneration {
	
	// Fenetre principale de l'application
	FenetrePrincipale parent;
	
	/**
	 * Constructeur de la fenêtre.
	 * @param parent Fenetre principale de l'application.
	 */
	public FenetreGeneration(FenetrePrincipale parent) {
		
		this.parent = parent;
		/* Elements permettant de saisir les valeurs */
		JTextField textNoeuds = new JTextField();        
        JTextField textDensite = new JTextField();        
        String[] angle = {"0°<=alpha<10°", "40°<=alpha<50°", "80°<=alpha<90°"};
        JComboBox<String> comboAngle = new JComboBox<>(angle);       
        JTextField textCommodite = new JTextField();
        
        //Crée une icône avec l'image "infos" et la redimensionne
        ImageIcon helpIcon = new ImageIcon("src"+System.getProperty("file.separator")+"img"+System.getProperty("file.separator")+"infos.png");
        Image img = helpIcon.getImage(); 
        Image newimg = img.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH);  
        helpIcon = new ImageIcon(newimg);
        
        /* Crée les labels avec le texte, l'icône et un texte pour donner des infos. */
        JLabel Nodes = new JLabel("Nodes:");
        Nodes.setIcon(helpIcon);
        //Ajoute un texte d'explication 
        Nodes.setToolTipText("<html>Nombre de noeuds à générer. <br/>Doit être un entier > 8.</html>");

        JLabel Density = new JLabel("Density:");
        Density.setIcon(helpIcon);
        Density.setToolTipText("<html>Densité du graphe à générer. <br/>Doit être une valeur comprise entre 0 et 1.</html>");
        
        JLabel Alpha = new JLabel("Alpha:");
        Alpha.setIcon(helpIcon);
        Alpha.setToolTipText("<html>Angle alpha .....</html>");
        
        JLabel Commodities = new JLabel("Commodities:");
        Commodities.setIcon(helpIcon); 
        Commodities.setToolTipText("<html>Nombre de commodités à générer. Une commodité représente un transport à effectuer dans le réseau que l'on génère.<br/>Doit être un entier > 8.</html>");
        
        /* Ajoute tous les labels dans le panel qui va être affiché */
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(Nodes);
        panel.add(textNoeuds);
        panel.add(Density);
        panel.add(textDensite);
        panel.add(Alpha);
        panel.add(comboAngle);
        panel.add(Commodities);
        panel.add(textCommodite);
        int result = JOptionPane.showConfirmDialog(null, panel,"Generate",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        boolean valable = true;
        /* Si l'utilisateur valide, on vérifie que toutes les valeurs sont correctes avant de lancer la génération */
        if (result == JOptionPane.OK_OPTION) {
        	try {
        		/* Teste si ce sont bien des chiffres et si les valeurs sont correctes */
        		Integer.parseInt(textNoeuds.getText());
        		Float.parseFloat(textDensite.getText());
        		Integer.parseInt(textCommodite.getText());
        		if (Integer.parseInt(textNoeuds.getText()) < 8 || Integer.parseInt(textCommodite.getText()) <= 0 || Float.parseFloat(textDensite.getText()) > 1 || Float.parseFloat(textDensite.getText()) < 0) {
        			valable = false;
        		}
        	} catch (NumberFormatException e) {
        		valable = false;
        	}
        	if (valable) {
        		generer(textNoeuds.getText(), textDensite.getText(), comboAngle.getSelectedItem().toString(), textCommodite.getText());
        	} else {
        		FenetreConsole.getFenetreConsole().printConsole("Invalid value.");
        	}
        } else {
            System.out.println("Cancelled");
        }
	}
	
	/* Utilise le generateur écrit en Julia our générer une instance random */
	public void generer(String noeuds, String densite, String angle, String commodites) {
		
		int angAlpha = 0;
		if (angle == "0°<=alpha<10°") {
			angAlpha = 0;
		} else if (angle == "40°<=alpha<50°") {
			angAlpha = 45;
		} else {
			angAlpha = 90;
		}
		ThreadGeneration t = new ThreadGeneration(noeuds, densite, String.valueOf(angAlpha), String.valueOf(commodites), parent);
		t.start();
		this.parent.chargement();
	}

}
