/*
 * ControleurFenetrePrincipale
 * 
 * Version 1.0
 */

package controleur;

import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import modele.Journee;
import modele.Plan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vue.VueFenetrePrincipale;

import java.util.*;

/**
 * Contrôleur de la vue principale de l'application.
 * @author H4113
 *
 */
@Getter
public class ControleurFenetrePrincipale {
    
    public static final Logger LOGGER = LogManager.getLogger(ControleurFenetrePrincipale.class);
	final VueFenetrePrincipale vue;

	// Etats
    Etat etatCourant;
    final EtatInitial etatInitial = new EtatInitial();
    final EtatSansDemande etatSansDemande = new EtatSansDemande();
    final EtatAvecDemande etatAvecDemande = new EtatAvecDemande();
    final EtatAfficherFeuillesRoute etatAfficherFeuillesRoute = new EtatAfficherFeuillesRoute();
    final EtatDemandeLivraisonSelectionneeAvecTournees etatDemandeLivraisonSelectionneeAvecTournees = new EtatDemandeLivraisonSelectionneeAvecTournees();
    final EtatDemandeLivraisonSelectionneeSansTournees etatDemandeLivraisonSelectionneeSansTournees = new EtatDemandeLivraisonSelectionneeSansTournees();
    final EtatModifierDemandeLivraisonSansTournees etatModifierDemandeLivraisonSansTournees = new EtatModifierDemandeLivraisonSansTournees();
    final EtatSaisieNouvelleDemandeAvecTournees etatSaisieNouvelleDemandeAvecTournees = new EtatSaisieNouvelleDemandeAvecTournees();
    final EtatSaisieNouvelleDemandeSansTournees etatSaisieNouvelleDemandeSansTournees = new EtatSaisieNouvelleDemandeSansTournees();
    final EtatTourneesCalculees etatTourneesCalculees = new EtatTourneesCalculees();
	final EtatSelectionLivraisonPourNouvelleDemande etatSelectionLivraisonPourNouvelleDemande = new EtatSelectionLivraisonPourNouvelleDemande();

	// Map qui associe les états à l'état des boutons sur lesquels on peut cliquer
	private HashMap<Etat, ArrayList<Button>> boutonsActivesParEtat;

	// modèle
	Journee journee;
	Plan planCharge;

	public ControleurFenetrePrincipale(VueFenetrePrincipale vue) {
		this.vue = vue;

		boutonsActivesParEtat = new HashMap<Etat, ArrayList<Button>>() {{
			put(etatAfficherFeuillesRoute, new ArrayList<>(Arrays.asList(

			)));
			put(etatAvecDemande, new ArrayList<>(Arrays.asList(
				vue.buttonChargerPlan,
				vue.buttonAutoriserAjouterLivraison,
				vue.buttonChargerDemandes,
				vue.buttonSauvegarderDemandes,
				vue.buttonCalculerTournees
			)));
			put(etatDemandeLivraisonSelectionneeSansTournees, new ArrayList<>(Arrays.asList(
				vue.buttonModifierLivraison,
				vue.buttonSupprimerLivraison
			)));
			put(etatDemandeLivraisonSelectionneeAvecTournees, new ArrayList<>(Arrays.asList(
				vue.buttonModifierLivraison,
				vue.buttonSupprimerLivraison,
				vue.buttonAssignerNvLivreur
			)));
			put(etatInitial, new ArrayList<>(Arrays.asList(
				vue.buttonChargerPlan
			)));
			put(etatModifierDemandeLivraisonSansTournees, new ArrayList<>(Arrays.asList(
				vue.buttonValiderLivraison,
				vue.buttonAnnulerLivraison
			)));
			put(etatSaisieNouvelleDemandeSansTournees, new ArrayList<>(Arrays.asList(
				vue.buttonValiderLivraison,
				vue.buttonAnnulerLivraison
			)));
			put(etatSaisieNouvelleDemandeAvecTournees, new ArrayList<>(Arrays.asList(
				vue.buttonValiderLivraison,
				vue.buttonAnnulerLivraison
			)));
			put(etatSansDemande, new ArrayList<>(Arrays.asList(
				vue.buttonChargerPlan,
				vue.buttonAutoriserAjouterLivraison,
				vue.buttonChargerDemandes
			)));
			put(etatSelectionLivraisonPourNouvelleDemande, new ArrayList<>(Arrays.asList(

			)));
			put(etatTourneesCalculees, new ArrayList<>(Arrays.asList(
				vue.buttonAfficherFeuillesRoute,
				vue.buttonAutoriserAjouterLivraison
			)));
		}};

		this.changementEtat(etatInitial);
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

	public void assignerAutreLivreur() {
		etatCourant.assignerAutreLivreur(this);
	}

	public void modifierDemande() {
		etatCourant.modifierDemande(this);
	}

	public void chargerPlan() throws Exception {
		etatCourant.chargerPlan(this);
	}

	public void clicGaucheSurPlan(MouseEvent event) {
		etatCourant.clicGaucheSurPlan(this, event);
	}

	public void supprimerLivraison() {
		etatCourant.supprimerLivraison(this);
	}

	public void chargerListeDemandes() throws Exception{
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
		this.vue.activerExclusivementBoutons(boutonsActivesParEtat.get(nouvelEtat));
		LOGGER.debug("Nouvel état : " + nouvelEtat.getClass().getName());
	}

	public void clicSurLivreur() {
		System.out.println("appel dans la vue");
		etatCourant.clicSurLivreur(this);}
}
