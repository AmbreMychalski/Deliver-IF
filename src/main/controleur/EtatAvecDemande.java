package controleur;

import javafx.scene.input.MouseEvent;

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
        this.naviguerSurPlan(c, event);
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
        boolean tourneeComplete = this.calculerEtAfficherTournee(c);
        if(!tourneeComplete) {
            vue.PopUpTourneeImpossible.display(c);
            c.changementEtat(c.etatTourneesCalculeesPartielles);
            System.out.println("changement d'état : "+c.etatCourant);
        }
        else {
            c.changementEtat(c.etatTourneesCalculees);
        }
        c.vue.tableViewDemandesLivraison.setVisible(false);
        c.vue.tableViewLivraisons.setVisible(true);
    }
}
