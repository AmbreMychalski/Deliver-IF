package modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DemandeLivraison {
	private Intersection intersection;
	private int [] plageHoraire;
}
