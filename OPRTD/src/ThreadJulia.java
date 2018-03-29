import java.awt.Window;
import java.io.IOException;

public class ThreadJulia extends Thread {

	String path;
	Instance parent;
	
	public ThreadJulia(String path, Instance parent) {
		this.path = path;
		this.parent = parent;
	}
	
    public void run(){
    	Runtime runtime = Runtime.getRuntime();
		Process proc;
		try {
			proc = runtime.exec(new String[] { "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\bin\\julia.exe", "--depwarn=no" , "C:\\Users\\Jean\\AppData\\Local\\Julia-0.5.2\\ProjetM1\\belman.jl" , path+System.getProperty("file.separator")} );
			System.out.println(path+System.getProperty("file.separator"));
			System.out.println("Attente ... ");
			/* Récupère ce qui est écrit dans la console de Julia */
			AfficheurFluxJulia fluxSortie = new AfficheurFluxJulia(proc.getInputStream());
            AfficheurFluxJulia fluxErreur = new AfficheurFluxJulia(proc.getErrorStream());

            Thread t = new Thread(fluxSortie);
            t.start();
            new Thread(fluxErreur).start();

            /* On attend la fin de l'exécution du thread pour lire les résultats */
            proc.waitFor();
            parent.lectureResultats(fluxSortie.result);
            
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
  }