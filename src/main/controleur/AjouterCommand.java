package controleur;

import modele.DemandeLivraison;
import modele.Livreur;

public class AjouterCommand implements Command{

    private DemandeLivraison demande;
    private Livreur livreur;

    public AjouterCommand(DemandeLivraison d, Livreur l){
        demande = d;
        livreur = l;
    }
    public void doCommand(){
        livreur.ajouterDemandeLivraison(demande);
    }
    public void undoCommand (){
        livreur.supprimerDemandeLivraison(demande);
    }

}
