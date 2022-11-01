package controleur;

import modele.DemandeLivraison;

import java.util.List;

public class EtatTourneesCalculeesPartielles extends Etat {
    public  void supprimerLivraison(ControleurFenetrePrincipale c) {
        System.out.println("ouioui");
        List<DemandeLivraison> demandeLivraisonNonTraitees = c.journee.getDemandesLivraisonNonTraitees();

        for(DemandeLivraison dmd : demandeLivraisonNonTraitees){
            c.journee.supprimerDemandeLivraison(dmd);
            c.tableViewDemandesLivraison.getItems().remove(dmd);
        }
        c.tableViewDemandesLivraison.refresh();
        c.textfieldIdentifiantIntersectionSelection.setText("");
        c.textfieldPlageHoraire.setText("");
        c.afficherDemandeLivraison(true);
        c.etatCourant = c.etatTourneesCalculees;
        System.out.println(c.etatCourant);
    }
}
