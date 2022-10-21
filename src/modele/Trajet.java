package modele;

import java.util.List;

public class Trajet {
	List<Segment> segments;

	public Trajet(List<Segment> segments) {
		super();
		this.segments = segments;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}
	
	
}
