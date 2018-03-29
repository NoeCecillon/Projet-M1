import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import logs.FenetreConsole;

/* Utilisé pour éxécuter une commande sur Julia */
class AfficheurFluxJulia implements Runnable {

    private final InputStream inputStream;
    public String result;

    AfficheurFluxJulia(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

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
        //FenetreConsole.getFenetreConsole().printSimplex(result);
    }
}