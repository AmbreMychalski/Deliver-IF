package modele;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Getter
@Setter

public class Livreur extends Observable {
    private int numero;
    private Tournee tournee;
    private List<Livraison> livraisons = new ArrayList<>();
    private List<DemandeLivraison> demandeLivraisons = new ArrayList<>();
    static int nbLivreur = 0;

    public Livreur(Tournee tournee){
        nbLivreur++;
        numero = nbLivreur;
        this.tournee = tournee;
        livraisons = tournee.getLivraisons();
    }
    public Livreur(){
        nbLivreur++;
        numero = nbLivreur;
    }

    public String toString(){
        return Integer.toString(numero);
    }

    public void ajouterDemandeLivraison(DemandeLivraison livr) {
        this.getDemandeLivraisons().add(livr);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    public void modifierTournee(Tournee tournee){
        this.tournee = tournee;
        this.livraisons = tournee.getLivraisons();
        notifierObservateurs("ModificationTournee");
    }
    public void notifierObservateurs(Object arg){
        setChanged();
        notifyObservers(arg);
    }

    public void ajouterObservateur(Observer obj) {
        addObserver(obj);
    }

    public void supprimerTournee() {
        this.tournee = null;
        this.livraisons = new ArrayList<>();
        this.demandeLivraisons = new ArrayList<>();
        notifierObservateurs("SuppressionTournee");
    }

    public void supprimerDemandeLivraison(DemandeLivraison ligne) {
        this.demandeLivraisons.remove(ligne);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    public void supprimerLivraison(Livraison livr) {
        this.livraisons.remove(livr);
        this.demandeLivraisons.remove(livr.getDemandeLivraison());
        notifierObservateurs("ModificationTournee");
    }

    public void ajouterLivraisonTournee(int index, Livraison livr) {
        //t.getLivraisons().add(index+1,livr);
        this.tournee.getLivraisons().add(index, livr);
        notifierObservateurs("ModificationTournee");
    }

    public void modifierDemandeLivraison(DemandeLivraison demande, Intersection intersection, PlageHoraire plageHoraire) {
        demande.modifierDemandeLivraison(intersection, plageHoraire);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    /*
    public void ajouterLivraison(int index, Livraison livr) {
        livraisons.add(index+1,livr);
        notifierObservateurs("ModificationTournee");
    }
    */

}


