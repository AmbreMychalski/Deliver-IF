package modele;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Journee {
	private int nbMaxLivreur;
	private int nbLivreur;
	private Plan plan;
	private List<DemandeLivraison> demandesLivraison;
	private List<Tournee> tournees;
	//private TemplateTSP template;
	
	public void chargerDemandeLivraison(String fichier) {
		
	}
}
