package controleur;

import modele.DemandeLivraison;
import modele.Livraison;
import modele.Livreur;

/**
 * Implémentation de la commande liée à l'ajout
 */
public class CommandeAjouter implements Commande {

    private Livraison livraisonAvant;
    private Livreur livreur;
    private DemandeLivraison demandeLivraison;
    private Livraison livraison;
    ControleurFenetrePrincipale c;

    public CommandeAjouter(ControleurFenetrePrincipale c, Livreur livreur,
                           Livraison livraisonAvant, DemandeLivraison demandeLivraison) {
        this.livraisonAvant = livraisonAvant;
        this.livreur = livreur;
        this.demandeLivraison = demandeLivraison;
        this.c = c;
    }

    public void doCommande() {
        if(this.livraison == null) {
            this.livraison = c.getJournee().ajouterDemandeLivraisonTournee(
                    demandeLivraison, livraisonAvant, livreur);
        } else {
            c.getJournee().ajouterLivraisonTournee(
                    this.livraison, this.livraisonAvant, this.livreur);
        }

        c.vue.afficherLivraisons(livreur, true);
    }

    public void undoCommande() {
        c.getEtatCourant().supprimerLivraison(c, livreur, livraison);
    }
}
