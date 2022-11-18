package controleur;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livreur;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;
import vue.VueFenetrePrincipale;

import java.util.ArrayList;

/**
 * Classe implémentant l'état quand au moins une demande est chargée
 */
public class EtatAvecDemande extends Etat {
    public EtatAvecDemande() {
        super.message = "Ajoutez, chargez, sauvegardez ou " +
                "supprimez des demandes de livraisons ou " +
                "calculez les tournées";
    }

    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.tableViewDemandesLivraison.setDisable(true);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, false);

        if(intersectionTrouvee != null) {
            ArrayList<DemandeLivraison> demandesAssociees = new ArrayList<>();

            for (DemandeLivraison demande : c.vue.comboboxLivreur.getValue().getDemandeLivraisons()) {
                if (demande.getIntersection().equals(intersectionTrouvee)) {
                    demandesAssociees.add(demande);
                }
            }

            if (demandesAssociees.size() == 1) {
                c.vue.tableViewDemandesLivraison.getSelectionModel().select(demandesAssociees.get(0));
                this.selectionnerDemande(c, false);
                c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
            } else if (demandesAssociees.size() > 1) {
                FenetrePlusieursLivraisonsAuMemeEndroit.display(c, demandesAssociees, null, false);
            }
        }
    }

    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c, false);

        if (demandeSelectionee) {
            c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
        }
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception {
        chargerDemandes(c);
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }

    public boolean calculerTournees(ControleurFenetrePrincipale c) {
        boolean tourneeCalculee;

        long startTime = System.currentTimeMillis();
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        tourneeCalculee = c.journee.calculerTournee(livreur);

        ControleurFenetrePrincipale.LOGGER.debug("tourneeCalculee = "
                + tourneeCalculee);
        ControleurFenetrePrincipale.LOGGER.debug("Solution trouvé en :"
                + (System.currentTimeMillis() - startTime) + "ms ");
        
        if (tourneeCalculee) {
            c.vue.afficherLivraisons(livreur, true);
            c.changementEtat(c.etatTourneesCalculees);
            c.vue.tableViewDemandesLivraison.setVisible(false);
            c.vue.tableViewLivraisons.setVisible(true);
            this.majComboboxLivreur(c);
        } else {
            c.vue.labelGuideUtilisateur.setText("Il y a trop de demandes pour calculer la tournée");
        }

        return tourneeCalculee;
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }

    public void zoomScroll(ControleurFenetrePrincipale c, ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if(deltaY > 0){
            c.vue.redessinerPlan(true,1.5);
        } else {
            c.vue.redessinerPlan(true,0.6667);
        }

        if(c.vue.comboboxLivreur.getValue().getTournee() != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(),
                    true);
        } else {
            c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(),
                    true);
        }

        if(!c.vue.textfieldIdentifiantIntersection.getText().isEmpty()) {
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
