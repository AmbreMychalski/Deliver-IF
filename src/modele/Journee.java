package modele;

import java.util.List;

public class Journee {
	private int nbMaxLivreur;
	private int nbLivreur;
	private Plan plan;
	private List<DemandeLivraison> demandesLivraison;
	private List<Tournee> tournees;
	//private TemplateTSP template;
	
	public Journee(int nbMaxLivreur, int nbLivreur, Plan plan, List<DemandeLivraison> demandesLivraison,
			List<Tournee> tournees) {
		super();
		this.nbMaxLivreur = nbMaxLivreur;
		this.nbLivreur = nbLivreur;
		this.plan = plan;
		this.demandesLivraison = demandesLivraison;
		this.tournees = tournees;
	}
	
	public void chargerDemandeLivraison(String fichier) {
		
	}
	
	public int getNbMaxLivreur() {
		return nbMaxLivreur;
	}

	public void setNbMaxLivreur(int nbMaxLivreur) {
		this.nbMaxLivreur = nbMaxLivreur;
	}

	public int getNbLivreur() {
		return nbLivreur;
	}

	public void setNbLivreur(int nbLivreur) {
		this.nbLivreur = nbLivreur;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public List<DemandeLivraison> getDemandesLivraison() {
		return demandesLivraison;
	}

	public void setDemandesLivraison(List<DemandeLivraison> demandesLivraison) {
		this.demandesLivraison = demandesLivraison;
	}

	public List<Tournee> getTournees() {
		return tournees;
	}

	public void setTournees(List<Tournee> tournees) {
		this.tournees = tournees;
	}
	
	
	
}
