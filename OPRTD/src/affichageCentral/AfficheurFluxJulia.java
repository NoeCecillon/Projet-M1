package affichageCentral;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import logs.FenetreConsole;

/**
 * Permet de r�cup�rer les donn�es en sortie de Julia.
 */
public class AfficheurFluxJulia implements Runnable {

    private final InputStream inputStream;
    public String result;

    /**
     * Constructeur
     * @param inputStream Le flux de sortie du processus Julia (ce qui est �crit dans la console par Julia)
     */
    public AfficheurFluxJulia(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Construit et retourne un bufferedReader pour lire les donn�es de l'inputStream.
     * @param is Le flux que l'on veut lire.
     * @return Un BufferedReader pour lire les donn�es de l'inputStream donn� en param�tre.
     */
    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Lit les donn�es en sortie de Julia tant qu'il y en a et construit la String result contenant toutes ces infos.
     */
    @Override
    public void run() {
    	result = "";
        BufferedReader br = getBufferedReader(inputStream);
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
            	result = result + ligne + System.getProperty("line.separator");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}