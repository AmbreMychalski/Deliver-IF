package modele;

public class DemandeLivraison {
	private Intersection intersection;
	private int [] plageHoraire;
	
	public DemandeLivraison(Intersection intersection, int[] plageHoraire) {
		this.intersection = intersection;
		this.plageHoraire = plageHoraire;
	}

	public Intersection getIntersection() {
		return intersection;
	}

	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}

	public int[] getPlageHoraire() {
		return plageHoraire;
	}

	public void setPlageHoraire(int[] plageHoraire) {
		this.plageHoraire = plageHoraire;
	}
	
	
	
}
