package modele;

import lombok.*;

/**
 * Classe implémentant un segment, avec une origine, une destination, une longueur et
 * le nom de la rue/avenue/cour...
 */
@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
public class Segment {
	private Intersection origine;
	private Intersection destination;
	private float longueur;
	private String nom;
}

