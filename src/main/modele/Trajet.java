package modele;

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
	private Intersection origine;
	private Intersection destination;
	public Trajet(List<Segment> segments){
		this.segments = segments;
		this.origine = segments.get(0).getOrigine();
		this.destination = segments.get(segments.size()-1).getDestination();
	}
}
