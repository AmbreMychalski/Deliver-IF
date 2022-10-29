package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;

public class EtatSaisieNouvelleDemandeSansTournees extends Etat{

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }

    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.validerAjoutDemande(c);
        if (c.journee.getDemandesLivraison().size() > 0){
            c.etatCourant = c.etatAvecDemande;
        }
    }
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(true);
        c.buttonAnnulerLivraison.setDisable(true);
        c.comboboxPlageHoraire.setDisable(true);
        c.textfieldIdentifiantIntersection.setText("");
        if (c.journee.getDemandesLivraison().size() == 0) {
            c.etatCourant = c.etatSansDemande;
            c.buttonCalculerTournees.setDisable(true);
            c.buttonSauvegarderDemandes.setDisable(true);
        } else {
            c.etatCourant = c.etatAvecDemande;
        }
    }
    
}
