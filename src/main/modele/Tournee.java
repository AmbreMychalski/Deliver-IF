package modele;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Classe implémentant une tournée, avec la liste de trajets qu'elle contient,
 * et la liste des livraisons qui la composent
 */
@Getter
@Setter
@AllArgsConstructor
public class Tournee {
	private List<Trajet> trajets;
	private List<Livraison> livraisons;
}
