package affichageCentral;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import logs.FenetreConsole;

/**
 * Permet de récupérer les données en sortie de Julia.
 */
public class AfficheurFluxJulia implements Runnable {

    private final InputStream inputStream;
    public String result;

    /**
     * Constructeur
     * @param inputStream Le flux de sortie du processus Julia (ce qui est écrit dans la console par Julia)
     */
    public AfficheurFluxJulia(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Construit et retourne un bufferedReader pour lire les données de l'inputStream.
     * @param is Le flux que l'on veut lire.
     * @return Un BufferedReader pour lire les données de l'inputStream donné en paramètre.
     */
    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Lit les données en sortie de Julia tant qu'il y en a et construit la String result contenant toutes ces infos.
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