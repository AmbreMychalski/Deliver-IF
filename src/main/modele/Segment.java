package modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Segment {
	private Intersection origine;
	private Intersection destination;
	private float longueur;
	private String nom;
}

