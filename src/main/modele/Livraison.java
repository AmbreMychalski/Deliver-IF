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

	public String getHeureAffiche() {


		int h = (int) heure;
		int min = (int) (60 * (heure - h));
		//return String.valueOf(h)+"h"+String.valueOf(min);
		return ((int) (heure) + "h" + ((int) ((heure - (int) heure) * 60) < 10 ? "0" : "") + (int) ((heure - (int) heure) * 60));
	}

}
