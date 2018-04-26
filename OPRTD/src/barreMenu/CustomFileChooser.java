package barreMenu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * FileChooser légèrement modifié pour ajouter 1 bouton "Aide".
 * Utilisé par menuBar quand on veut ajouter une nouvelle méthode.
 */
class CustomFileChooser extends JFileChooser{
	
	/**
	 * Construit un FileChooser avec un bouton aide en plus.
	 * @param texteAide Le texte qui sera affiché au clic sur le bouton aide.
	 */
    public CustomFileChooser(String texteAide) {
        JButton boutonAide = new JButton("Aide");
        //Action au clic sur le bouton d'aide
        boutonAide.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  //Affiche le texte d'aide.
        		  JOptionPane.showMessageDialog(null, texteAide);
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

   }
}