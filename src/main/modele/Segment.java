package modele;

import lombok.*;

@EqualsAndHashCode
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

