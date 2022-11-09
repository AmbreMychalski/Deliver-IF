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

	public Trajet(List<Segment> seg, float longueur){
		this.segments = new ArrayList<Segment>(seg);
		this.depart = segments.get(0).getOrigine();
		this.arrivee = segments.get(segments.size()-1).getDestination();
		this.longueur = longueur;
	}

}
