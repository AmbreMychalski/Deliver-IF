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

    public void ListeDeCommandes(){
        i=0;
    }

    public void ajouterCommand (Commande c){
        listeCommandes.add(i,c);
        c.doCommande();
        i++;
    }
    public void undoCommande(){
        if (i >= 0) {
            listeCommandes.get(i-1).undoCommande();;
            i--;
        }
    }
    public  void redoCommande(){
        listeCommandes.get(i-1).doCommande();
        i++;
    }

    public Commande getDerniereComande(){
        return listeCommandes.get(i-1);
    }
}
