package controleur;

public interface Etat {

	public void chargerPlan();
	
	public void ajouterDemande();
	
	public void clicGaucheSurPlan();
	
	public void choixPlageHoraire();
	
	public void ajouter();
	
	public void annuler();
	
	public void valider();
	
	public void chargerListeDemandes();
	
	public void supprimerDemande();
	
	public void sauvegarderDemandes();
	
	public void calculerTournees();
	
	public void ajoutValide();
	
	public void ajoutInvalide();
	
	public void afficherFeuillesRoute();
	
	public void sauvegarderFeuilesRoute();
	
	public void fermerFenetre();
	
	public void quitterLogiciel();
}
