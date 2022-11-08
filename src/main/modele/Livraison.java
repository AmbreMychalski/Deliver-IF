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
	private float heure;
	private int livreur;

	public Long getIdIntersectionLivraison() {
		return demandeLivraison.getIntersection().getIdIntersection();
	}

	public PlageHoraire getPlageHoraireLivraison() {
		return demandeLivraison.getPlageHoraire();
	}

	public String getHeureAffiche(){
		int h = (int) heure;
		return String.valueOf(h)+"h";
	}

}
