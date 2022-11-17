package modele;

public class ComparateurHeureLivraison implements java.util.Comparator<String> {

    /**
     * Traduit les heures (sous forme de String) en int avant de les comparer.
     * @param o1 L'heure par rapport à laquelle on compare
     * @param o2 L'heure que l'on compare
     * @return retourne la différence entre la première et la seconde.
     */
    @Override
    public int compare(String o1, String o2) {
        int  heure1 = Integer.parseInt(o1.split("h")[0]);
        int  heure2 = Integer.parseInt(o2.split("h")[0]);
        int minute1 = Integer.parseInt(o1.split("h")[1]);
        int minute2 = Integer.parseInt(o2.split("h")[1]);

        if(heure1 != heure2) {
            return heure1 - heure2;
        } else {
            return minute1 - minute2;
        }
    }
}
