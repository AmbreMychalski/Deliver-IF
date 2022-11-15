package controleur;

import modele.DemandeLivraison;
import modele.Livreur;

public class AjouterCommande implements Commande {

    private DemandeLivraison demande;
    private Livreur livreur;
    ControleurFenetrePrincipale c;

    public AjouterCommande(DemandeLivraison demande, Livreur livreur, ControleurFenetrePrincipale c){
        this.demande = demande;
        this.livreur = livreur;
        this.c = c;
    }
    public void doCommande(){
        livreur.ajouterDemandeLivraison(demande);
    }
    public void undoCommande (){
        c.supprimerLivraison();
    }

}
