/* Utilisé dans AffichageReseauReel pour représenter une paire de sommets départ/arrivée pour un arc */

public class paireNoeuds {

	final String x;
	final String y;
		  
	public paireNoeuds(String x, String y) {
		this.x = x;
		this.y = y;			  
	}
	
	@Override
	public boolean equals(Object obj) {
		paireNoeuds p = (paireNoeuds) obj;
		if ((p.x.equals(this.x) && p.y.equals(this.y)) || (p.y.equals(this.x) && p.x.equals(this.y))) {
			return true;
		}
		return false;
	}

}
