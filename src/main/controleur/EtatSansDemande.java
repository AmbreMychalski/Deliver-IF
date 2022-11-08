package controleur;

import java.io.File;
import java.util.ArrayList;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import modele.DemandeLivraison;
import modele.Intersection;

public class EtatSansDemande extends Etat{
    public EtatSansDemande() {
        super.message = "EtatSansDemande";
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws FichierNonConformeException {
        c.etatInitial.chargerPlan(c);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.buttonValiderLivraison.setDisable(false);
        c.vue.buttonAnnulerLivraison.setDisable(false);
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        this.chargerDemandes(c);
        c.changementEtat(c.etatAvecDemande);
        c.vue.buttonCalculerTournees.setDisable(false);
        c.vue.buttonSauvegarderDemandes.setDisable(false);
    }
}
