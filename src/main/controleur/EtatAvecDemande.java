package controleur;

import javafx.scene.input.MouseEvent;
import modele.Livraison;

import java.util.List;

public class EtatAvecDemande extends Etat{
    public EtatAvecDemande() {
        super.message = "Ajoutez, chargez, sauvegardez ou " +
                "supprimez des demandes de livraisons ou " +
                "calculez les tournées";
    }
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.buttonValiderLivraison.setDisable(false);
        c.vue.buttonAnnulerLivraison.setDisable(false);
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.tableViewDemandesLivraison.setDisable(true);
        c.vue.buttonCalculerTournees.setDisable(true);
        c.vue.buttonChargerDemandes.setDisable(true);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, false);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c,false);
        if(demandeSelectionee){
            c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
        }
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception{
        chargerDemandes(c);
        c.vue.buttonCalculerTournees.setDisable(false);
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        int livreur = c.vue.comboboxLivreur.getValue();
        long startTime = System.currentTimeMillis();
        boolean tourneeComplete = c.journee.calculerTournee();
        ControleurFenetrePrincipale.logger.debug("tourneeComplete = " + tourneeComplete);
        ControleurFenetrePrincipale.logger.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");

        List<Livraison> listeLivraisons = c.journee.getLivraisonsLivreur(livreur);
        c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
        c.vue.tableViewLivraisons.refresh();

        this.afficherTournee(c,c.journee.getTournees().get(livreur-1));
        c.vue.buttonAfficherFeuillesRoute.setDisable(false);
        c.vue.buttonCalculerTournees.setDisable(true);
        c.vue.buttonChargerDemandes.setDisable(true);
        c.changementEtat(c.etatTourneesCalculees);
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
        majComboboxLivreur(c); //temporaire
    }
}
