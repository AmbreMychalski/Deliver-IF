package controleur;

import modele.DemandeLivraison;
import modele.Livreur;

public class AjouterCommande implements Commande {

    private DemandeLivraison demande;
    private Livreur livreur;

    public AjouterCommande(DemandeLivraison d, Livreur l){
        demande = d;
        livreur = l;
    }
    public void doCommande(){
        livreur.ajouterDemandeLivraison(demande);
    }
    public void undoCommande (){
        livreur.supprimerDemandeLivraison(demande);
    }

}
