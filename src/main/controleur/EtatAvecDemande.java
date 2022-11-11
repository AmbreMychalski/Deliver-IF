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
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.tableViewDemandesLivraison.setDisable(true);
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
    }

    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        this.sauvegarderListeDemandes(c);
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        int livreur = c.vue.comboboxLivreur.getValue();
        long startTime = System.currentTimeMillis();
        boolean tourneeComplete = c.journee.calculerTournee();
        ControleurFenetrePrincipale.LOGGER.debug("tourneeComplete = " + tourneeComplete);
        ControleurFenetrePrincipale.LOGGER.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");

        List<Livraison> listeLivraisons = c.journee.getLivraisonsLivreur(livreur);
        c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
        c.vue.tableViewLivraisons.refresh();

        this.afficherTournee(c,c.journee.getTournees().get(livreur-1));
        c.changementEtat(c.etatTourneesCalculees);
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
        majComboboxLivreur(c); //temporaire
    }
}
