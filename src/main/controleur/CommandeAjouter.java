package controleur;

import modele.DemandeLivraison;
import modele.Livraison;
import modele.Livreur;

public class CommandeAjouter implements Commande {

    private Livraison livraisonAvant;
    private Livreur livreur;
    private DemandeLivraison demandeLivraison;
    private  Livraison livraison;
    ControleurFenetrePrincipale c;

    public CommandeAjouter(ControleurFenetrePrincipale c, Livreur livreur, Livraison livraisonAvant, DemandeLivraison demandeLivraison){
        this.livraisonAvant = livraisonAvant;
        this.livreur = livreur;
        this.demandeLivraison = demandeLivraison;
        this.c = c;
    }
    public void doCommande(){
        this.livraison = c.getJournee().ajouterDemandeLivraisonTournee(demandeLivraison, livraisonAvant, livreur);
        c.vue.afficherLivraisons(livreur, true);
    }
    public void undoCommande (){
        c.getEtatCourant().supprimerLivraison(c, livreur, livraison);
    }

}
