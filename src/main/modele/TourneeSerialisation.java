package modele;

import lombok.AllArgsConstructor;

import java.io.*;
import java.util.*;

@AllArgsConstructor
public class TourneeSerialisation {
    Plan plan;
    List<String> coordNames = Arrays.asList("N", "NE","E","SE", "S", "SW", "W", "NW", "N");
    Map<String, String> coord = new HashMap<String, String>(){{
        put("N","Nord");
        put("NE","Nord-Est");
        put("E","Est");
        put("SE","Sud-Est");
        put("S","Sud");
        put("SW","Sud-Ouest");
        put("W","Ouest");
        put("NW","Nord-Ouest");
    }};
    String[] mot_liaison = new String[]{" vers "," sur "," pour suivre ",", "," pour rejoindre "};
    StringWriter  writer = new StringWriter();
    PrintWriter      out = new PrintWriter(writer);

    public TourneeSerialisation(Plan plan) {
        this.plan = plan;
    }
    public String serialiser(Livreur livreur) throws IndexOutOfBoundsException {
        out.println("Sérialiseur de tournée");
        int i = 1;
        Tournee tournee = livreur.getTournee();

        out.println("***********************************************************");
        out.println("Tournée du livreur : " + tournee.getLivraisons().get(0).getLivreur());
        out.println("************* Liste des livraisons et horaire de livraison : *********** \n");

        for(Livraison liv : tournee.getLivraisons()) {
            List<String> rues = plan.obtenirRuesIntersection(liv.getDemandeLivraison().getIntersection());
            out.println(i + "/ Croisement de la " + rues.get(0) + " et de la "
                    + rues.get(1) + " à " + liv.getHeureAffichee());
            i++;
        }

        out.println("*******************************************************");
        out.println("****************** Itinéraire détaillé ****************");
        out.println("La tournée est composée de " + tournee.getTrajets().size() + " trajets : \n");

        int j = 1;

        for(Trajet trajet : tournee.getTrajets()) {
            out.println("///--- /" + j + "/ de "
                    + plan.obtenirRuesIntersection(trajet.getDepart())+ " à "
                    + plan.obtenirRuesIntersection(trajet.getArrivee())
                    + "-----/// \n");

            int                      a = 0;
            String directionPrecedente = null;
            String       ruePrecedente = null;
            float                somme = 0;
            boolean      premiereSomme = true;
            String     phraseDirection = null;

            for(Segment segment : trajet.getSegments()) {
                String direction = bearing(segment.getOrigine().getLatitude(),
                        segment.getOrigine().getLongitude(),
                        segment.getDestination().getLatitude(),
                        segment.getDestination().getLongitude());

                if(a == 0) {
                    out.print((a + 1) + "/ ");
                    out.print("Prenez la rue " + segment.getNom() + " direction "+ coord.get(direction) + " sur ");
                    somme = segment.getLongueur();
                    a++;
                } else {
                    if (segment.getNom().equals(ruePrecedente)) {
                        somme += segment.getLongueur();
                    } else {
                        if (premiereSomme) {
                            out.println((int) somme + " mètres.");
                            premiereSomme = false;
                            phraseDirection = direction(directionPrecedente,direction);
                        } else {
                            out.print((a + 1) + "/ ");
                            out.println(phraseDirection
                                    + mot_liaison[(int) (Math.random() * (mot_liaison.length - 1))]
                                    + ruePrecedente
                                    + " sur " + (int) somme + " mètres.");
                            a++;
                            phraseDirection = direction(directionPrecedente, direction);
                        }
                        somme = segment.getLongueur();
                    }
                    if(segment == trajet.segments.get(trajet.getSegments().size()-1)) {
                        out.print((a + 1) + "/ ");
                        out.println(direction(directionPrecedente, direction)
                                + mot_liaison[(int) (Math.random() * (mot_liaison.length - 1))]
                                + segment.getNom()
                                + " sur " + (int) somme + " mètres.");
                    }
                }

                ruePrecedente = segment.getNom();
                directionPrecedente = direction;
            }

            j++;

            out.println();
            out.println("*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/");
            out.println();
        }
        out.close();
        return writer.toString();
    }

    public void sauvegarderDansFichier(File fichier) throws IOException {
         FileWriter fw = new FileWriter(fichier);

         fw.write(writer.toString());
         fw.close();
    }


    //https://stackoverflow.com/questions/9457988/bearing-from-one-coordinate-to-another
    private String bearing(double lat1, double lon1, double lat2, double lon2) {
        double      latitude1 = Math.toRadians(lat1);
        double      latitude2 = Math.toRadians(lat2);
        double       longDiff = Math.toRadians(lon2 - lon1);
        double              y = Math.sin(longDiff)*Math.cos(latitude2);
        double              x = Math.cos(latitude1)*Math.sin(latitude2)
                                - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
        double   resultDegree = (Math.toDegrees(Math.atan2(y, x))+360)%360;
        double    directionId = Math.round(resultDegree / 45);

        if (directionId < 0) {
            directionId = directionId + 8;
        }

        return this.coordNames.get((int) directionId);
    }

    private String direction (String card1, String card2) {
        int    diffIndex;
        String directive;

        int indexCard1 = coordNames.indexOf(card1);
        int indexCard2 = coordNames.indexOf(card2);

        if(Objects.equals(card1, card2)) {
            diffIndex = 0;
        } else if(Objects.equals(card1, "N")) {
            if(Math.abs(indexCard2) < Math.abs(indexCard2 - (coordNames.size() - 1))) {
                diffIndex = indexCard2 - 0;
            } else {
                diffIndex = indexCard2 - (coordNames.size() - 1);
            }
        } else if(Objects.equals(card2, "N")) {
            if(Math.abs(0 - indexCard1) < Math.abs((coordNames.size() - 1) - indexCard1)) {
                diffIndex = 0 - indexCard1;
            } else {
                diffIndex = (coordNames.size() - 1) - indexCard1;
            }
        } else {
            diffIndex = indexCard2 - indexCard1;
            if (Math.abs(diffIndex) == 5) {
                diffIndex = (diffIndex < 0 ? 3 : -3);
            }
            if (Math.abs((diffIndex)) == 6) {
                diffIndex = (diffIndex < 0 ? 2 : -2);
            }
        }

        String[] adverbes = new String[]{"", " légèrement", "", " complètement", ""};

        if (Math.abs(diffIndex) != 4 && Math.abs(diffIndex) != 0) {
            directive = "Tournez" + adverbes[Math.abs(diffIndex)]
                    + (diffIndex < 0 ? " à gauche" : " à droite");
        } else if (Math.abs(diffIndex) == 0) {
            directive = "Continuez tout droit";
        } else if (Math.abs(diffIndex) == 4) {
            directive = "Faites presque demi-tour";
        } else{
            directive = "Aucune idée, j'ai bug mdr";
        }
        return directive;
    }
}
