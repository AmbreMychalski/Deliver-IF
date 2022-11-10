package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EtatDemandeLivraisonSelectionneeSansTournees extends Etat {
    public EtatDemandeLivraisonSelectionneeSansTournees() {
        super.message = "Cliquez sur le plan ou appuyez sur échap " +
                "pour quitter la sélection";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c,false);
        c.changementEtat(c.etatAvecDemande);
        c.vue.buttonCalculerTournees.setDisable(false);
        c.vue.buttonChargerDemandes.setDisable(false);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,false);
    }
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        this.supprimerDemandeLivraison(c);
        this.sortieDeSelectionDemande(c,false);
        if(c.journee.getDemandesLivraison().size() > 0){
            c.changementEtat(c.etatAvecDemande);
            c.vue.buttonCalculerTournees.setDisable(false);
            c.vue.buttonChargerDemandes.setDisable(false);
        } else {
            c.vue.buttonChargerDemandes.setDisable(false);
            c.vue.buttonSauvegarderDemandes.setDisable(true);
            c.vue.buttonCalculerTournees.setDisable(true);
            c.changementEtat(c.etatSansDemande);
        }
    }
    public void modifierDemande(ControleurFenetrePrincipale c) {
        this.modifierDemandeApresSelection(c);
        c.changementEtat(c.etatModifierDemandeLivraisonSansTournees);
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {

        switch (ke.getCode()) {
            case ESCAPE:
                this.sortieDeSelectionDemande(c,false);
                c.changementEtat(c.etatAvecDemande);
                c.vue.buttonCalculerTournees.setDisable(false);
                c.vue.buttonChargerDemandes.setDisable(false);
                break;
            case DELETE:
                this.supprimerDemandeLivraison(c);
                this.sortieDeSelectionDemande(c,false);
                c.vue.buttonCalculerTournees.setDisable(false);
                c.vue.buttonChargerDemandes.setDisable(false);
                if(c.journee.getDemandesLivraison().size() > 0){
                    c.changementEtat(c.etatAvecDemande);
                    c.vue.buttonCalculerTournees.setDisable(false);
                    c.vue.buttonChargerDemandes.setDisable(false);
                } else {
                    c.vue.buttonSauvegarderDemandes.setDisable(true);
                    c.vue.buttonCalculerTournees.setDisable(true);
                    c.vue.buttonChargerDemandes.setDisable(false);
                    c.changementEtat(c.etatSansDemande);
                }
                break;
        }
    }
}
