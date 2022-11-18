package controleur;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import vue.VueFenetrePrincipale;

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
        if(!c.vue.textfieldIdentifiantIntersection.getText().isEmpty()){
            long idIntersection = Long.parseLong(c.vue.textfieldIdentifiantIntersection.getText());

            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    c.journee.getPlan().getIntersections().get(idIntersection),
                    Color.DARKORCHID,
                    c.vue.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.CERCLE
            );
        }
    }
}
