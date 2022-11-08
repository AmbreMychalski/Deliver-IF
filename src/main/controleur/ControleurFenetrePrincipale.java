/*
 * ControleurFenetrePrincipale
 * 
 * Version 1.0
 */

package controleur;

import exception.FichierNonConformeException;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import modele.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vue.VueFenetrePrincipale;

import java.sql.SQLOutput;

/**
 * Contrôleur de la vue principale de l'application.
 * @author H4113
 *
 */
@Getter
public class ControleurFenetrePrincipale {
    
    public static final Logger logger = LogManager.getLogger(ControleurFenetrePrincipale.class);
	final VueFenetrePrincipale vue;

	// Etats
    Etat etatCourant;
    final EtatInitial etatInitial = new EtatInitial();
    final EtatSansDemande etatSansDemande = new EtatSansDemande();
    final EtatAvecDemande etatAvecDemande = new EtatAvecDemande();
    final EtatAfficherFeuillesRoute etatAfficherFeuillesRoute = new EtatAfficherFeuillesRoute();
    final EtatDemandeLivraisonSelectionneeAvecTournees etatDemandeLivraisonSelectionneeAvecTournees = new EtatDemandeLivraisonSelectionneeAvecTournees();
    final EtatDemandeLivraisonSelectionneeSansTournees etatDemandeLivraisonSelectionneeSansTournees = new EtatDemandeLivraisonSelectionneeSansTournees();
    final EtatModifierDemandeLivraisonAvecTournees etatModifierDemandeLivraisonAvecTournees = new EtatModifierDemandeLivraisonAvecTournees();
    final EtatModifierDemandeLivraisonSansTournees etatModifierDemandeLivraisonSansTournees = new EtatModifierDemandeLivraisonSansTournees();
    final EtatSaisieNouvelleDemandeAvecTournees etatSaisieNouvelleDemandeAvecTournees = new EtatSaisieNouvelleDemandeAvecTournees();
    final EtatSaisieNouvelleDemandeSansTournees etatSaisieNouvelleDemandeSansTournees = new EtatSaisieNouvelleDemandeSansTournees();
    final EtatTourneesCalculees etatTourneesCalculees = new EtatTourneesCalculees();

	final EtatTourneesCalculeesPartielles etatTourneesCalculeesPartielles = new EtatTourneesCalculeesPartielles();
	
	

	// modèle
	Journee journee;
	Plan planCharge;


	public ControleurFenetrePrincipale(VueFenetrePrincipale vue) {
		this.vue = vue;
		this.etatCourant = this.etatInitial;
		this.journee = new Journee();
		this.journee.ajouterObservateur(vue);
	}


	public void touchePressee(KeyEvent ke) {
		etatCourant.touchePressee(this, ke);
	}

	public void supprimerDemande() {
		etatCourant.supprimerDemande(this);
	}

	public void clicGaucheSurTableau(MouseEvent event) {
		etatCourant.clicGaucheSurTableau(this);
	}

	public void annulerAjouterOuModifier() {
		etatCourant.annulerAjouterOuModifier(this);
	}

	public void ajouterDemande() {
		etatCourant.ajouterDemande(this);
	}

	public void ajouterLivreur() {
		etatCourant.ajouterLivreur(this);
	}

	public void modifierDemande() {
		etatCourant.modifierDemande(this);
	}

	public void chargerPlan() throws FichierNonConformeException {
		etatCourant.chargerPlan(this);
	}

	public void clicGaucheSurPlan(MouseEvent event) {
		etatCourant.clicGaucheSurPlan(this, event);
	}

	public void supprimerLivraison() {
		etatCourant.supprimerLivraison(this);
	}

	public void chargerListeDemandes() {
		etatCourant.chargerListeDemandes(this);
	}

	public void calculerTournees() {
		etatCourant.calculerTournees(this);
	}

	public void validerAjouterOuModifier() {
		etatCourant.validerAjouterOuModifier(this);
	}

	public void sauvegarderDemandes() {
		etatCourant.sauvegarderDemandes(this);
	}

	public void changementEtat(Etat nouvelEtat){
		this.etatCourant = nouvelEtat;
		this.vue.updateLabelGuideUtilisateur(this.etatCourant.getMessage());
	}
}
