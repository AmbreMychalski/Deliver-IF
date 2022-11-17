package controleur;

import modele.Livraison;
import modele.Livreur;

public class CommandeSupprimer implements Commande {
    private Livreur livreur;
    private  Livraison livraison;
    private Livraison livraisonAvant;
    ControleurFenetrePrincipale c;

    public CommandeSupprimer(ControleurFenetrePrincipale c, Livreur livreur,
                             Livraison livraison) {
        this.livraison = livraison;

        if(livreur.getLivraisons().indexOf(livraison) - 1 >= 0) {
            this.livraisonAvant = livreur.getLivraisons().get(livreur.getLivraisons().indexOf(livraison) - 1);
        } else {
            this.livraisonAvant = null;
        }

        this.livreur = livreur;
        this.c = c;
    }

    public void doCommande() {
        c.getEtatCourant().supprimerLivraison(c, livreur, livraison);
    }

    public void undoCommande (){
        c.getJournee().ajouterLivraisonTournee(livraison, livraisonAvant, livreur);
        c.vue.afficherLivraisons(livreur, true);
    }

}
