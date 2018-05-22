package barreMenu;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import aide.FenetreAide;

/**
 * FileChooser légèrement modifié pour ajouter 1 bouton "Aide".
 * Utilisé par menuBar quand on veut ajouter une nouvelle méthode.
 */
public class CustomFileChooser extends JFileChooser{
	
	/* Bouton "Annuler" permettant de fermer la fenêtre de sélection */
	JButton boutonAnnuler;
	
	/**
	 * Construit un FileChooser avec un bouton aide en plus.
	 * @param ancre L'ancre vers laquelle on sera redirigé lors de l'ouverture de la fenêtre d'aide.
	 */
    public CustomFileChooser(String ancre) {
        JButton boutonAide = new JButton("Aide");
        //Action au clic sur le bouton d'aide
        boutonAide.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  //On ferme la fenêtre de sélection car elle est modale donc on ne peut pas afficher la fenêtre d'aide par dessus.
        		  fermer();
        		  //On ouvre la fenêtre d'aide en positionnant la vue sur l'ancre donnée en paramètre
        		  new FenetreAide("Aide", ancre);
        		  
        	  } 
        });
        JPanel panel1 = (JPanel)this.getComponent(3);
        JPanel panel2 = (JPanel) panel1.getComponent(3);

        //récupère les 2 boutons existants de base puis supprime tout.
        Component c1=panel2.getComponent(0);
        Component c2=panel2.getComponent(1);
        panel2.removeAll();
        //ajoute notre nouveau bouton puis les 2 qui étaient là de base.
        panel2.add(boutonAide);
        panel2.add(c1);
        panel2.add(c2);
        //Garde une référence sur le bouton "Annuler"
        boutonAnnuler = (JButton) c2;
   }
    
    /**
     * Méthode permettant de simuler un clic sur le bouton annuler pour fermer ce JFileChooser.
     */
    public void fermer() {
    	this.boutonAnnuler.doClick();
    }
}