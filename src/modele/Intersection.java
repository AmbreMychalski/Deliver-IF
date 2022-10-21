package modele;

public class Intersection {
	private Long idIntersection;
	private float latitude;
	private float longitude;
	
	public Intersection(Long idIntersection, float latitude, float longitude) {
		this.idIntersection = idIntersection;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String toString() {
		return("Intersection n° "+this.idIntersection +"  : lat/long "+this.latitude+"/"+this.longitude);
	}
	
	public Long getIdIntersection() {
		return idIntersection;
	}
	
	public void setIdIntersection(Long idIntersection) {
		this.idIntersection = idIntersection;
	}
	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
