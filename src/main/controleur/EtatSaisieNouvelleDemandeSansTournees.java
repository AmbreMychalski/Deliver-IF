package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;

public class EtatSaisieNouvelleDemandeSansTournees extends Etat{

    public EtatSaisieNouvelleDemandeSansTournees() {
        super.message = "EtatSaisieNouvelleDemandeSansTournees";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        boolean ajoutOK = this.validerAjoutDemande(c);
        if(ajoutOK){
            c.vue.buttonCalculerTournees.setDisable(false);
            c.changementEtat(c.etatAvecDemande);
        }
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        if (c.journee.getDemandesLivraison().size() == 0) {
            c.changementEtat(c.etatSansDemande);
            c.vue.buttonCalculerTournees.setDisable(true);
            c.vue.buttonSauvegarderDemandes.setDisable(true);
        } else {
            c.changementEtat(c.etatAvecDemande);
        }
    }
    
}
