package modele;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tournee {
	private List<Trajet> trajets;
	private List<Livraison> livraisons;
}
