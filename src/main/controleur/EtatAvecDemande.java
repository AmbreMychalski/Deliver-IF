package controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Segment;
import modele.Tournee;
import modele.Trajet;

public class EtatAvecDemande extends Etat{

    
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.tableViewDemandesLivraison.setDisable(true);
        c.etatCourant = c.etatSaisieNouvelleDemandeSansTournees;
    }
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        this.chargerDemandes(c);
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        this.calculerEtAfficherTournee(c);
        c.buttonCalculerTournees.setDisable(true);
        c.buttonChargerDemandes.setDisable(true);
        c.etatCourant = c.etatTourneesCalculees;
    }
}
