import com.mxgraph.model.mxCell;

/* Représente un arc présent dans le graphe avec origine, destination et l'objet mxCell */
public class Arc {

	public int origine;
	public int destination;
	public mxCell arc;
	
	public Arc(int o, int d, mxCell a) {
		this.origine = o;
		this.destination = d;
		this.arc = a;
	}
	
	public Arc(int o, int d) {
		this.origine = o;
		this.destination = d;
	}
	
	/* Permet de vérifier si un sommet est l'origine ou la destination de cet arc */
	public boolean contains(int val) {
		if (origine == val || destination == val) {
			/* On place le sommet recherché comme étant le sommet de destination */
			if (destination != val) {
				int tmp = destination;
				destination = origine;
				origine = tmp;
			}
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "("+origine+","+destination+") ";
	}
}
