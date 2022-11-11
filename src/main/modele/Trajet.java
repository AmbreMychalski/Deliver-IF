package modele;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Trajet {
	List<Segment> segments;
	Intersection depart;
	Intersection arrivee;
	float longueur;

	public Trajet(List<Segment> segments, float longueur) {
		this.segments = new ArrayList<Segment>(segments);
		this.depart   = this.segments.get(0).getOrigine();
		this.arrivee  = this.segments.get(this.segments.size()-1).getDestination();
		this.longueur = longueur;
	}

}
