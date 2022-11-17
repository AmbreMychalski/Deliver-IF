package controleur;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class EtatSansDemande extends Etat{
    public EtatSansDemande() {
        super.message = "Ajoutez ou chargez des demandes de livraison";
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }

    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, false);
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception {
        try {
            this.chargerDemandes(c);
            c.changementEtat(c.etatAvecDemande);
        } catch (Exception ex) {
            c.changementEtat(c.etatSansDemande);
            throw new Exception(ex);
        }
    }

    @Override
    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

    public void zoomScroll(ControleurFenetrePrincipale c, ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if(deltaY>0){
            c.vue.redessinerPlan(true,1.5);
        }
        else{
            c.vue.redessinerPlan(true,0.6667);
        }

        if(c.vue.comboboxLivreur.getValue().getTournee() != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(),
                    true);
        } else {
            c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(),
                    true);
        }
    }
}
