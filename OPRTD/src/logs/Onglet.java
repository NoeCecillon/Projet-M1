package logs;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneLayout;
import javax.swing.text.BadLocationException;

/* Onglet dans le menu des logs */
public class Onglet extends JScrollPane{

	/* Zone de texte */
	JTextArea contenu;
	
	public Onglet() {		
		setLayout(new ScrollPaneLayout());
		this.contenu = new JTextArea();
		/* Fenetre de texte non éditable */
		this.contenu.setEditable(false);
		contenu.setBounds(0, 0, 10000, 10000);
		setViewportView(contenu);
		add(new JProgressBar());
		/* Pour éviter des problèmes de redimensionnement de la console il vaut mieux que la text area contienne au moins 50 lignes donc on commence en la remplissant avec des lignes vides */
		for (int i=0; i<60; i++) {
			contenu.append(System.getProperty("line.separator"));
		}
	}
	
	public void print(String texte) {
		contenu.setText(texte + System.getProperty("line.separator")+contenu.getText());
	}
	
}
