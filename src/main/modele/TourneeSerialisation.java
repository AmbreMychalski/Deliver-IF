package modele;

import javafx.stage.FileChooser;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@AllArgsConstructor
public class TourneeSerialisation {

    Tournee tournee;
    PrintStream out;
    File fichier;
    public TourneeSerialisation(Tournee tournee){
        this.tournee = tournee;
        try {
            fichier = new File("./data/fdr.txt");
            out = System.out;//new PrintStream(fichier);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void serialiser(){
        out.println("Bienvenue sur la super sérialiseur de tournée");
        out.println("**********************************************");
        out.println("Liste des livraisons et horaire de livraison: \n");
        int i=0;
        for(Livraison liv :tournee.getLivraisons()){
            out.println(i+"/ Intersection : "+liv.getDemandeLivraison().getIntersection()+" à "+dateToString(liv.getHeure()));
            i++;
        }
        out.println("************************************************");
    }

    public String dateToString(float date){
        return ((int)(date)+"h"+(int)((date-(int)date)*60));
    }
}
