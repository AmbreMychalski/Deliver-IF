package modele;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Livraison {
	private DemandeLivraison demandeLivraison;
	private float date;
	private int livreur;

	public Livraison(Livraison livraison) {
		this.date = livraison.getDate();
		this.demandeLivraison = livraison.getDemandeLivraison();
		this.livreur = livraison.getLivreur();
	}
}
