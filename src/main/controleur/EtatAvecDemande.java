package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;
import modele.Livreur;

import java.util.List;

public class EtatAvecDemande extends Etat{
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
        DemandeLivraison demandeAssociee = null;
        for(DemandeLivraison demande : c.vue.comboboxLivreur.getValue().getDemandeLivraisons()){
            if(intersectionTrouvee == demande.getIntersection()){
                demandeAssociee = demande;
            }
        }
        if(demandeAssociee != null) {
            c.vue.tableViewDemandesLivraison.getSelectionModel().select(demandeAssociee);
            this.selectionnerDemande(c, false);
            c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
        }
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c,false);
        if(demandeSelectionee){
            c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
        }
    }

    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception{
        chargerDemandes(c);
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        long startTime = System.currentTimeMillis();
        Livreur livreur =  c.vue.comboboxLivreur.getValue();
        boolean tourneeComplete;
         tourneeComplete = c.journee.calculerTournee(livreur);

        ControleurFenetrePrincipale.LOGGER.debug("tourneeComplete = " + tourneeComplete);
        ControleurFenetrePrincipale.LOGGER.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");

        this.afficherTournee(c, livreur.getTournee());
        c.changementEtat(c.etatTourneesCalculees);
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
        this.majComboboxLivreur(c);
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }

}
