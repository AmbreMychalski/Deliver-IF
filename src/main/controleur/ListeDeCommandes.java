package controleur;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListeDeCommandes {
    private int i;
    private List<Commande> listeCommandes = new ArrayList<>();

    public ListeDeCommandes(){
        i=0;
    }

    public void ajouterCommande (Commande c){
        while(i != listeCommandes.size()){
            listeCommandes.remove(i);
        }
        listeCommandes.add(i,c);
        c.doCommande();
        i++;
    }
    public void undoCommande(){
        if (i > 0) {
            listeCommandes.get(i-1).undoCommande();
            i--;
        }
    }
    public  void redoCommande(){
        if(i < listeCommandes.size()) {
            listeCommandes.get(i).doCommande();
            i++;
        }
    }

    public Commande getDerniereComande(){
        return listeCommandes.get(i-1);
    }

    public void viderListeCommandes() {
        this.listeCommandes.clear();
        i=0;
    }
}
