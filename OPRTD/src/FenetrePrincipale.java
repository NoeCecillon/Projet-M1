import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logs.FenetreConsole;

public class FenetrePrincipale extends JFrame {

	private JPanel contentPane;
	private PanelCentre fenetreCentre;
	private FenetreInfos fenetreInfos;
	private FenetreConsole fenetreLog;
	private menuBar menu;
	private JDialog progressBar;
	
	/* Lancement de l'application. */
	public static void main(String[] args) {
		FenetrePrincipale frame = new FenetrePrincipale();
		frame.setVisible(true);	
	}

	/* Cr�ation de la fen�tre. */
	public FenetrePrincipale() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		/* Barre de menus. */
		this.menu = new menuBar(this);
		setJMenuBar(this.menu);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(new GridBagLayout());
		setContentPane(contentPane);

		/* Console en bas de page */
		this.fenetreLog = new FenetreConsole();
		GridBagConstraints gc6 = new GridBagConstraints();
		gc6.gridx = 0;
		gc6.gridy = 1;
		gc6.gridwidth = 2;
		gc6.fill = GridBagConstraints.BOTH;
		gc6.weighty = 0.2;
		contentPane.add(this.fenetreLog, gc6);
		
		/* On cr�e la fenetre centrale juste pour avoir un placement correct des �l�ments, ces objets seront inutiles ensuite */
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.ipady = gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 0;
		gc.gridy = 0;		
		gc.weightx = 0.8;
		gc.weighty = 0.8;
		this.fenetreCentre = new affichageRandom(0);
		contentPane.add(this.fenetreCentre, gc);
		
	}
	
	public void previsualiserInstance(String path, boolean instanceReelle) {
		
		/* Titre */
		TitledBorder border = new TitledBorder("Visualization");
		border.setTitleJustification(TitledBorder.LEFT);
		border.setTitlePosition(TitledBorder.TOP);
		/* On supprime l'objet provisoire qu'on avait ajout� pour r�server l'emplacement */
		contentPane.remove(this.fenetreCentre);		
		/* Ajout de la pr�visualisation de l'instance */
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.ipady = gc.anchor = GridBagConstraints.CENTER;
		gc.gridx = 0;
		gc.gridy = 0;		
		gc.weightx = 0.85;
		gc.weighty = 0.8;
		/* Si instanceReelle est true on cr�e une instance r�elle, sinon une random */
		if (instanceReelle) {
			this.fenetreCentre = new affichageReel(path);
		} else {
			this.fenetreCentre = new affichageRandom(path);
		}
		this.fenetreCentre.setBorder(border);
		contentPane.add(this.fenetreCentre, gc);
		//revalidate();
		
		/* Titre */
		TitledBorder border2 = new TitledBorder("Information");
		border2.setTitleJustification(TitledBorder.LEFT);
		border2.setTitlePosition(TitledBorder.TOP);
		/* On retire l'ancienne fenetre */
		if (this.fenetreInfos != null) {
			contentPane.remove(this.fenetreInfos);
		}
		/* Ajout de la fen�tre d'infos sur le c�t� */
		this.fenetreInfos = new FenetreInfos(fenetreCentre.getInstance());
		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx = 1;
		gc2.gridy = 0;
		gc2.fill = GridBagConstraints.BOTH;
		gc2.weightx = 0.15;
		gc2.weighty = 0.8;
		this.fenetreInfos.setBorder(border2);
		contentPane.add(this.fenetreInfos, gc2);
		/* Avant le calcul de la solution les boutons sont d�sactiv�s */
		fenetreInfos.desactiver();
		revalidate();		
	}
	
	/* Quand l'utilisateur demande de r�soudre l'instance on lance le calcul de la solution */
	public void resoudre() {
		this.fenetreCentre.getInstance().calculSolution(this);
	}
	
	public FenetreConsole getFenetreLog() {
		return this.fenetreLog;
	}
	
	public FenetreInfos getFenetreInfos() {
		return this.fenetreInfos;
	}
	
	public PanelCentre getFenetreCentre() {
		return this.fenetreCentre;
	}
	
	public void createFenetreGeneration() {
		new FenetreGeneration(this);
	}
	
	/* Affiche un pop up pour indiquer que le chargement est en cours */
	public void chargement() {
		JProgressBar progress = new JProgressBar();
		progress.setIndeterminate(true);
		JOptionPane optionPane = new JOptionPane(progress, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

		this.progressBar = new JDialog();
		progressBar.setTitle("Solving");
		progressBar.setModal(true);
		progressBar.setContentPane(optionPane);
		progressBar.setLocationRelativeTo(fenetreCentre);
		progressBar.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		progressBar.pack();
		progressBar.setVisible(true);
	}
	
	/* Enl�ve le pop up de chargement */
	public void finChargement() {
		progressBar.dispose();
	}
}
