package controleur;

import javafx.scene.input.MouseEvent;

public class EtatSaisieNouvelleDemandeSansTournees extends Etat{

    public EtatSaisieNouvelleDemandeSansTournees() {
        super.message = "Cliquez sur le plan pour choisir une " +
                "intersection, puis choisissez la plage horaire";
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
