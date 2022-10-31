package controleur;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import modele.DemandeLivraison;
import modele.Intersection;

public class EtatSansDemande extends Etat{

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        c.etatInitial.chargerPlan(c);
    }
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.buttonCalculerTournees.setDisable(false);
        c.etatCourant = c.etatSaisieNouvelleDemandeSansTournees;
    }
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        this.chargerDemandes(c);
        c.etatCourant = c.etatAvecDemande;
        c.buttonCalculerTournees.setDisable(false);
        c.buttonSauvegarderDemandes.setDisable(false);
    }
}
