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

	public Trajet(List<Segment> segments, float longueur, Intersection dep, Intersection arr) {
		this.segments = new ArrayList<>(segments);
		this.depart   = dep;
		this.arrivee  = arr;

		this.longueur = longueur;
	}

}
