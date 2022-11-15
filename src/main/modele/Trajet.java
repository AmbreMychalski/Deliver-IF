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

	/**
	 * Constructeur spécifique de la classe Trajet.
	 * @param segments La liste des segments que l'on veut ajouter
	 * @param longueur La longueur du trajet
	 */
	public Trajet(List<Segment> segments, float longueur) {
		this.segments = new ArrayList<>(segments);
		this.depart   = this.segments.get(0).getOrigine();
		this.arrivee  = this.segments.get(this.segments.size()-1).getDestination();
		this.longueur = longueur;
	}

}
