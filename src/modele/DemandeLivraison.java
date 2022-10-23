package modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DemandeLivraison {
	private Intersection intersection;
	private int [] plageHoraire;
}
