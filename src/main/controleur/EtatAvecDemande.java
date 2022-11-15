package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livreur;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;

import java.util.ArrayList;

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
        ArrayList<DemandeLivraison> demandesAssociees = new ArrayList<>();
        for (DemandeLivraison demande : c.vue.comboboxLivreur.getValue().getDemandeLivraisons()) {
            if (intersectionTrouvee == demande.getIntersection()) {
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

    public void calculerTournees(ControleurFenetrePrincipale c) {
        long startTime = System.currentTimeMillis();
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        boolean tourneeComplete;
        tourneeComplete = c.journee.calculerTournee(livreur);

        ControleurFenetrePrincipale.LOGGER.debug("tourneeComplete = " + tourneeComplete);
        ControleurFenetrePrincipale.LOGGER.debug("Solution trouvé en :" + (System.currentTimeMillis() - startTime) + "ms ");

        c.vue.afficherLivraisons(livreur, true);
        c.changementEtat(c.etatTourneesCalculees);
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
        this.majComboboxLivreur(c);
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }
}
