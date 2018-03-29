import com.mxgraph.model.mxCell;

/* Repr�sente un arc pr�sent dans le graphe avec origine, destination et l'objet mxCell */
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
	
	/* Permet de v�rifier si un sommet est l'origine ou la destination de cet arc */
	public boolean contains(int val) {
		if (origine == val || destination == val) {
			/* On place le sommet recherch� comme �tant le sommet de destination */
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
