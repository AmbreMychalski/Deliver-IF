package modele;

import java.util.Comparator;

public class ComparateurPlageHoraire implements Comparator<PlageHoraire> {

    @Override
    public int compare(PlageHoraire o1, PlageHoraire o2) {
        return o1.getDebut() - o2.getDebut();
    }
}
