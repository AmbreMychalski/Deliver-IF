package modele;

import java.util.List;

public class Tournee {
	private List<Trajet> trajets;
	private List<Livraison> livraisons;
	
	public Tournee(List<Trajet> trajets, List<Livraison> livraisons) {
		super();
		this.trajets = trajets;
		this.livraisons = livraisons;
	}

	public List<Trajet> getTrajets() {
		return trajets;
	}

	public void setTrajets(List<Trajet> trajets) {
		this.trajets = trajets;
	}

	public List<Livraison> getLivraisons() {
		return livraisons;
	}

	public void setLivraisons(List<Livraison> livraisons) {
		this.livraisons = livraisons;
	}
	
}
