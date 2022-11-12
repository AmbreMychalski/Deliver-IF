package controleur;

import javafx.scene.input.MouseEvent;
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
        long startTime = System.currentTimeMillis();
        Livreur livreur =  c.vue.comboboxLivreur.getValue();
        boolean tourneeComplete;
        if(livreur == null){
            livreur = c.creerLivreur();
            tourneeComplete = c.journee.calculerTournee(livreur);
        }else{
             tourneeComplete = c.journee.calculerTourneeNouveauLivreur(livreur);
        }

        ControleurFenetrePrincipale.LOGGER.debug("tourneeComplete = " + tourneeComplete);
        ControleurFenetrePrincipale.LOGGER.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");
        List<Livraison> listeLivraisons;
        if(livreur == null){
            listeLivraisons = c.journee.getLivreurs().get(0).getTournee().getLivraisons();
        }else{
            listeLivraisons = livreur.getLivraisons();
        }

        c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
        c.vue.tableViewLivraisons.refresh();

        if(livreur == null){
            this.afficherTournee(c, c.journee.getLivreurs().get(0).getTournee());
        }else{
            this.afficherTournee(c, livreur.getTournee());
        }
        c.changementEtat(c.etatTourneesCalculees);
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
        majComboboxLivreur(c); //temporaire
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        this.changerLivreur(c, livreur);
        if(livreur.getTournee() == null){
            c.changementEtat(c.etatAvecDemande);
            c.vue.buttonChargerDemandes.setDisable(true);
        }else{
            c.changementEtat(c.etatTourneesCalculees);
        }
    }

}
