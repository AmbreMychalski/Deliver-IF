package modele;

public class Segment {
	static Long idCommun=0l;
	Long id;
	Intersection origine;
	Intersection destination;
	float longueur;
	String nom;
	
	public Segment( Intersection origine, Intersection destination, float longueur, String nom) {
		this.id= Segment.idCommun;
		Segment.idCommun ++;
		
		this.origine = origine;
		this.destination = destination;
		this.longueur= longueur;
		this.nom = nom;
				
		
				
	}
	
	public String toString() {
		return("Segment n°"+this.id+" : Orgine "+this.origine.idIntersection +" | Destination :"+this.destination.idIntersection+" | Longueur = "+this.longueur+" Nom : "+this.nom);
	}
}

