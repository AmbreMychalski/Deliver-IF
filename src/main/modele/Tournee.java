package main.modele;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Tournee {
	private List<Trajet> trajets;
	private List<Livraison> livraisons;
}
