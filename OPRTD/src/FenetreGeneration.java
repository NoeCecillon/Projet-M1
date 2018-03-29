import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import logs.FenetreConsole;

public class FenetreGeneration {
	
	FenetrePrincipale parent;
	
	public FenetreGeneration(FenetrePrincipale parent) {
		
		this.parent = parent;
		/* Elements permettant de saisir les valeurs */
		JTextField textNoeuds = new JTextField();        
        JTextField textDensite = new JTextField();        
        String[] angle = {"0°<=alpha<10°", "40°<=alpha<50°", "80°<=alpha<90°"};
        JComboBox<String> comboAngle = new JComboBox<>(angle);       
        JTextField textCommodite = new JTextField();
        
        /* Crée les ? cliquable pour afficher des indications et les redimensionnent */
        ImageIcon helpIconNodes = new ImageIcon("src"+System.getProperty("file.separator")+"img"+System.getProperty("file.separator")+"help2.png");
        Image img = helpIconNodes.getImage(); 
        Image newimg = img.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);  
        helpIconNodes = new ImageIcon(newimg);
        
        ImageIcon helpIconDensity = new ImageIcon("src"+System.getProperty("file.separator")+"img"+System.getProperty("file.separator")+"help2.png");
        img = helpIconDensity.getImage(); 
        newimg = img.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);  
        helpIconDensity = new ImageIcon(newimg);
        
        ImageIcon helpIconAlpha = new ImageIcon("src"+System.getProperty("file.separator")+"img"+System.getProperty("file.separator")+"help2.png");
        img = helpIconAlpha.getImage(); 
        newimg = img.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);  
        helpIconAlpha = new ImageIcon(newimg);
        
        ImageIcon helpIconCommodities = new ImageIcon("src"+System.getProperty("file.separator")+"img"+System.getProperty("file.separator")+"help2.png");
        img = helpIconCommodities.getImage(); 
        newimg = img.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);  
        helpIconCommodities = new ImageIcon(newimg);
        
        /* Crée les labels avec le texte et le ? */
        JLabel Nodes = new JLabel("Nodes:");
        JLabel NodesIc = new JLabel(helpIconNodes); 
        NodesIc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Yay you clicked me");
            }
        });
        //Ajoute les 2 labels dans un panel (pour pouvoir mettre un listener sur l'icone seulement et pas sur icone + texte
        JPanel pNodes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //pNodes.add(NodesIc);
        pNodes.add(Nodes);
        /* Message pour donner des indications */
        pNodes.setToolTipText("<html>Nombre de noeuds à générer. <br/>Doit être un entier > 7.</html>");

        JLabel Density = new JLabel("Density:");
        JLabel DensityIc = new JLabel(helpIconDensity);
        /* Action pour afficher un message d'information quand on clique sur le ? */
        DensityIc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	JOptionPane.showMessageDialog(null, "Nombre de noeuds à générer."+ System.getProperty("line.separator") +"Doit être un entier > 7.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JPanel pDensity = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pDensity.add(DensityIc);
        pDensity.add(Density);
        
        JLabel Alpha = new JLabel("Alpha:");
        JLabel AlphaIc = new JLabel(helpIconAlpha);
        JPanel pAlpha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pAlpha.add(AlphaIc);
        pAlpha.add(Alpha);
        
        JLabel Commodities = new JLabel("Commodities:");
        JLabel CommoditiesIc = new JLabel(helpIconCommodities);
        JPanel pCommodities = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pCommodities.add(CommoditiesIc);
        pCommodities.add(Commodities);        
        
        /* Ajoute tous les panels dans le panel qui va être affiché */
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(pNodes);
        panel.add(textNoeuds);
        panel.add(pDensity);
        panel.add(textDensite);
        panel.add(pAlpha);
        panel.add(comboAngle);
        panel.add(pCommodities);
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
