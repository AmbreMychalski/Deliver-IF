package modele;

public class Segment {
	
	private Intersection origine;
	private Intersection destination;
	private float longueur;
	private String nom;
	
	public Segment( Intersection origine, Intersection destination, float longueur, String nom) {		
		this.origine = origine;
		this.destination = destination;
		this.longueur= longueur;
		this.nom = nom;
				
	}
	
	public Intersection getOrigine() {
		return origine;
	}

	public void setOrigine(Intersection origine) {
		this.origine = origine;
	}

	public Intersection getDestination() {
		return destination;
	}

	public void setDestination(Intersection destination) {
		this.destination = destination;
	}

	public float getLongueur() {
		return longueur;
	}

	public void setLongueur(float longueur) {
		this.longueur = longueur;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String toString() {
		return("Segment : Orgine "+this.origine.getIdIntersection() +" | Destination :"+this.destination.getIdIntersection()+" | Longueur = "+this.longueur+" Nom : "+this.nom);
	}
}

