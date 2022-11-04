package controleur;

import modele.DemandeLivraison;

import java.util.List;

public class EtatTourneesCalculeesPartielles extends Etat {
    public  void supprimerLivraison(ControleurFenetrePrincipale c) {
        System.out.println("ouioui");
        List<DemandeLivraison> demandeLivraisonNonTraitees = c.journee.getDemandesLivraisonNonTraitees();

        for(DemandeLivraison dmd : demandeLivraisonNonTraitees){
            c.journee.supprimerDemandeLivraison(dmd);
            c.vue.tableViewDemandesLivraison.getItems().remove(dmd);
        }
        c.vue.tableViewDemandesLivraison.refresh();
        c.vue.textfieldIdentifiantIntersectionSelection.setText("");
        c.vue.textfieldPlageHoraire.setText("");
        c.vue.afficherDemandeLivraison(true);
        c.etatCourant = c.etatTourneesCalculees;
        System.out.println(c.etatCourant);
    }
}
