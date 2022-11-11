package modele;

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

	private boolean dansSaPlageHoraire;

	/**
	 * Renvoie l'id de l'intersection de la livraison (utilisé pour
	 * l'affichage dans le TableView de la vue).
	 * @return id de la l'intersection
	 */
	public Long getIdIntersectionLivraison() {
		return demandeLivraison.getIntersection().getIdIntersection();
	}

	public PlageHoraire getPlageHoraireLivraison() {
		return demandeLivraison.getPlageHoraire();
	}

	public String getHeureAffichee() {
		int h = (int) heure;
		int min = (int) (60 * (heure - h));
		return ((int) (heure) + "h"
				+ ((int) ((heure - (int) heure) * 60) < 10 ? "0" : "")
				+ (int) ((heure - (int) heure) * 60));
	}

	public Livraison(Livraison livraison) {
		this.heure = livraison.getHeure();
		this.demandeLivraison = livraison.getDemandeLivraison();
		this.livreur = livraison.getLivreur();
		this.dansSaPlageHoraire = livraison.dansSaPlageHoraire;
	}

}
