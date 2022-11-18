/*
 * ControleurFenetrePrincipale
 * 
 * Version 1.0
 */

package controleur;

import javafx.scene.control.Control;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import lombok.Getter;
import modele.Journee;
import modele.Livreur;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import vue.VueFenetrePrincipale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Contrôleur de la vue principale de l'application.
 * @author H4113
 *
 */
@Getter
public class ControleurFenetrePrincipale {

	public static final Logger LOGGER = LogManager.getLogger(ControleurFenetrePrincipale.class);
	final VueFenetrePrincipale vue;

	private ListeDeCommandes listeCommandes = new ListeDeCommandes();

	// Etats
	Etat etatCourant;
	final EtatInitial etatInitial = new EtatInitial();
	final EtatSansDemande etatSansDemande = new EtatSansDemande();
	final EtatAvecDemande etatAvecDemande = new EtatAvecDemande();
	final EtatDemandeLivraisonSelectionneeAvecTournees etatDemandeLivraisonSelectionneeAvecTournees =
			new EtatDemandeLivraisonSelectionneeAvecTournees();
	final EtatDemandeLivraisonSelectionneeSansTournees etatDemandeLivraisonSelectionneeSansTournees =
			new EtatDemandeLivraisonSelectionneeSansTournees();
	final EtatModifierDemandeLivraisonSansTournees etatModifierDemandeLivraisonSansTournees =
			new EtatModifierDemandeLivraisonSansTournees();
	final EtatSaisieNouvelleDemandeAvecTournees etatSaisieNouvelleDemandeAvecTournees =
			new EtatSaisieNouvelleDemandeAvecTournees();
	final EtatSaisieNouvelleDemandeSansTournees etatSaisieNouvelleDemandeSansTournees =
			new EtatSaisieNouvelleDemandeSansTournees();
	final EtatTourneesCalculees etatTourneesCalculees = new EtatTourneesCalculees();
	final EtatSelectionLivraisonPourNouvelleDemande etatSelectionLivraisonPourNouvelleDemande =
			new EtatSelectionLivraisonPourNouvelleDemande();

	// Map qui associe les états à l'état des boutons sur lesquels on peut cliquer
	private final HashMap<Etat, ArrayList<Control>> controlsActivesParEtat;

	// modèle
	Journee journee;

	public ControleurFenetrePrincipale(VueFenetrePrincipale vue) {
		this.vue = vue;

		// Initilisation et configuration du LOGGER
		final LoggerContext context = (LoggerContext) LogManager.getContext(false);
		final org.apache.logging.log4j.core.config.Configuration config = context.getConfiguration();

		config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.ERROR);
		config.getLoggerConfig(ControleurFenetrePrincipale.class.getPackage().getName()).setLevel(Level.ERROR);
		context.updateLoggers(config);

		controlsActivesParEtat = new HashMap<Etat, ArrayList<Control>>() {{
			put(etatAvecDemande, new ArrayList<>(Arrays.asList(
					vue.buttonAutoriserAjouterLivraison,
					vue.buttonChargerDemandes,
					vue.buttonSauvegarderDemandes,
					vue.buttonCalculerTournees,
					vue.comboboxLivreur,
					vue.buttonNouveauLivreur,
					vue.buttonReinitAffPlan,
					vue.buttonChargerPlan
			)));
			put(etatDemandeLivraisonSelectionneeSansTournees, new ArrayList<>(Arrays.asList(
					vue.buttonModifierLivraison,
					vue.buttonSupprimerLivraison
			)));
			put(etatDemandeLivraisonSelectionneeAvecTournees, new ArrayList<>(Arrays.asList(
					vue.buttonSupprimerLivraison,
					vue.comboboxAssignerLivreur
			)));
			put(etatInitial, new ArrayList<>(Arrays.asList(
					vue.buttonChargerPlan
			)));
			put(etatModifierDemandeLivraisonSansTournees, new ArrayList<>(Arrays.asList(
					vue.buttonValiderLivraison,
					vue.buttonAnnulerLivraison,
					vue.comboboxPlageHoraire
			)));
			put(etatSaisieNouvelleDemandeSansTournees, new ArrayList<>(Arrays.asList(
					vue.buttonValiderLivraison,
					vue.buttonAnnulerLivraison,
					vue.comboboxPlageHoraire
			)));
			put(etatSaisieNouvelleDemandeAvecTournees, new ArrayList<>(Arrays.asList(
					vue.comboboxPlageHoraire,
					vue.buttonValiderLivraison,
					vue.buttonAnnulerLivraison
			)));
			put(etatSansDemande, new ArrayList<>(Arrays.asList(
					vue.buttonChargerPlan,
					vue.buttonAutoriserAjouterLivraison,
					vue.buttonChargerDemandes,
					vue.comboboxLivreur,
					vue.buttonNouveauLivreur,
					vue.buttonReinitAffPlan
			)));
			put(etatSelectionLivraisonPourNouvelleDemande, new ArrayList<>(Arrays.asList(

			)));
			put(etatTourneesCalculees, new ArrayList<>(Arrays.asList(
					vue.buttonAfficherFeuillesRoute,
					vue.buttonAutoriserAjouterLivraison,
					vue.comboboxLivreur,
					vue.buttonNouveauLivreur,
					vue.buttonChargerPlan,
					vue.buttonReinitAffPlan
			)));
		}};

		this.changementEtat(etatInitial);

		this.journee = new Journee();
		this.journee.ajouterObservateur(vue);
		Livreur liv = new Livreur();

		liv.ajouterObservateur(vue);
		this.journee.getLivreurs().add(liv);

		etatCourant.majComboboxLivreur(this);
		this.vue.comboboxLivreur.getSelectionModel().selectFirst();
	}

	public void viderListeDeCommandes() {
		this.listeCommandes.viderListeCommandes();
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

	public void chargerListeDemandes() throws Exception {
		etatCourant.chargerListeDemandes(this);
	}

	public boolean calculerTournees() {
		return etatCourant.calculerTournees(this);
	}

	public void validerAjouterOuModifier() {
		etatCourant.validerAjouterOuModifier(this);
	}

	public void sauvegarderDemandes() {
		etatCourant.sauvegarderDemandes(this);
	}

	public void changementEtat(Etat nouvelEtat) {
		this.etatCourant = nouvelEtat;

		this.vue.updateLabelGuideUtilisateur(this.etatCourant.getMessage());
		this.vue.activerExclusivementBoutons(controlsActivesParEtat.get(nouvelEtat));

		LOGGER.debug("Nouvel état : " + nouvelEtat.getClass().getName());
	}

	public void clicSurLivreur() {
		etatCourant.clicSurLivreur(this);
	}

	public Livreur creerLivreur() {
		Livreur livreur = new Livreur();

		livreur.ajouterObservateur(vue);
		journee.ajouterLivreur(livreur);

		return livreur;
	}

	public void actionClicComboboxAssignerLivreur() {
		etatCourant.clicSurComboboxAssignerLivreur(this);
	}

	public void majComboBox(){
		this.etatCourant.majComboboxLivreur(this);
	}
	public void zoomScroll(ScrollEvent event) {
		etatCourant.zoomScroll(this, event);
	}
}
