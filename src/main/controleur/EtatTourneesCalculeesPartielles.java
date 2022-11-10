package controleur;

public class EtatTourneesCalculeesPartielles extends Etat {
    public EtatTourneesCalculeesPartielles() {
        super.message = "EtatTourneesCalculeesPartielles";
    }

    public  void supprimerLivraison(ControleurFenetrePrincipale c) {
        System.out.println("ouioui");
        /*List<DemandeLivraison> demandeLivraisonNonTraitees = c.journee.getDemandesLivraisonNonTraitees();

        for(DemandeLivraison dmd : demandeLivraisonNonTraitees){
            c.vue.tableViewDemandesLivraison.getItems().remove(dmd);
            c.journee.supprimerDemandeLivraison(dmd);
        }
        c.vue.tableViewDemandesLivraison.refresh();
        c.vue.textfieldIdentifiantIntersectionSelection.setText("");
        c.vue.textfieldPlageHoraire.setText("");
        c.vue.afficherDemandeLivraison(true);
        c.changementEtat(c.etatTourneesCalculees);*/
    }
}
