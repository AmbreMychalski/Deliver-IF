package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public abstract class Etat {

	public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {}
	
	public void ajouterDemande(ControleurFenetrePrincipale c){}
	
	public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event){}
	
	public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}
	
	public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
	
	public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void chargerListeDemandes(ControleurFenetrePrincipale c) {}
	
	public void supprimerDemande(ControleurFenetrePrincipale c) {}
	
	public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
	
	public void calculerTournees(ControleurFenetrePrincipale c) {}
	
	public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public void fermerFenetre(ControleurFenetrePrincipale c) {}
	
	public void quitterLogiciel(ControleurFenetrePrincipale c) {}
	
	public void modifierDemande(ControleurFenetrePrincipale c) {}
	
	public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}
	
	private void annulerModif(ControleurFenetrePrincipale c) {
	    c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.buttonValiderLivraison.setDisable(true);
        c.buttonAnnulerLivraison.setDisable(true);
        c.comboboxPlageHoraire.setDisable(true);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
	}
}
