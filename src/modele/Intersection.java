package modele;

public class Intersection {
	Long idIntersection;
	float latitude;
	float longitude;
	
	public Intersection(Long idIntersection, float latitude, float longitude) {
		this.idIntersection = idIntersection;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String toString() {
		return("Intersection n° "+this.idIntersection +"  : lat/long "+this.latitude+"/"+this.longitude);
	}
}
