package main.controleur;

import javafx.scene.input.MouseEvent;

public interface Etat {

	public default void chargerPlan(ControleurFenetrePrincipale c) {}
	
	public default void ajouterDemande(ControleurFenetrePrincipale c) {}
	
	public default void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {}
	
	public default void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}
	
	public default void choixPlageHoraire(ControleurFenetrePrincipale c) {}
	
	public default void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public default void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
	    c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.buttonValiderLivraison.setDisable(true);
        c.buttonAnnulerLivraison.setDisable(true);
        c.comboboxPlageHoraire.setDisable(true);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
	}
	
	public default void chargerListeDemandes(ControleurFenetrePrincipale c) {}
	
	public default void supprimerDemande(ControleurFenetrePrincipale c) {}
	
	public default void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
	
	public default void calculerTournees(ControleurFenetrePrincipale c) {}
	
	public default void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public default void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public default void fermerFenetre(ControleurFenetrePrincipale c) {}
	
	public default void quitterLogiciel(ControleurFenetrePrincipale c) {}
	
	public default void modifierDemande(ControleurFenetrePrincipale c) {}
}
