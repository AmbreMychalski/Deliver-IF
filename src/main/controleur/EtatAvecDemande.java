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
        c.buttonCalculerTournees.setDisable(true);
        c.etatCourant = c.etatSaisieNouvelleDemandeSansTournees;
    }
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c);
        if(demandeSelectionee){
            c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
        }
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        this.chargerDemandes(c);
        c.buttonCalculerTournees.setDisable(false);
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        boolean tourneeComplete = this.calculerEtAfficherTournee(c);
        if(!tourneeComplete) {
            vue.PopUpTourneeImpossible.display(c);
            c.etatCourant = c.etatTourneesCalculeesPartielles;
            System.out.println("changement d'état : "+c.etatCourant);
        }
        else {
            c.buttonCalculerTournees.setDisable(true);
            c.buttonChargerDemandes.setDisable(true);
            c.etatCourant = c.etatTourneesCalculees;
        }
    }
}
