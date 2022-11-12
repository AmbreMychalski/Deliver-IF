package modele;

import lombok.*;
import vue.VueFenetrePrincipale;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Getter
@Setter

public class Livreur extends Observable {
    private int numero;
    private Tournee tournee;
    private List<Livraison> livraisons;
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

    public void setTournee(Tournee tournee){
        this.tournee = tournee;
        this.livraisons = tournee.getLivraisons();
    }
    public String toString(){
        return Integer.toString(numero);
    }

    public void ajouterDemandeLivraison(DemandeLivraison livr) {
        this.getDemandeLivraisons().add(livr);
        notifierObservateurs("AjoutDemandeLivraison");
    }
    public void notifierObservateurs(Object arg){
        setChanged();
        notifyObservers(arg);
    }

    public void ajouterObservateur(Observer obj) {
        addObserver(obj);
    }
}


