package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LivraisonTest {

    Intersection intersection = new Intersection(1L, 0F, 1F);
    PlageHoraire plageHoraire = new PlageHoraire(11, 12);

    DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);
    Livreur livreur = new Livreur();
    Livraison livraison = new Livraison(demandeLivraison, (float) 11.25, livreur, true);

    @Test
    @DisplayName("Test de l'horaire")
    void getHeureAfficheeTest() {
        String heureAffichee = "11h15";
        Assertions.assertEquals(livraison.getHeureAffichee(), heureAffichee);
    }

    @Test
    @DisplayName("Test de la récupération de la plage horaire")
    void getPlageHoraireTest() {
        Assertions.assertEquals(plageHoraire, livraison.getPlageHoraireLivraison());
    }

    @Test
    @DisplayName("Test de l'id de l'intersection de la livraison")
    void getIdIntersectionTest() {
        Assertions.assertEquals(1L, livraison.getIdIntersectionLivraison());
    }

    @Test
    @DisplayName("Test du constructeur de copie de la livraison")
    void LivraisonCopieTest() {
        Livraison livraison2 = new Livraison(livraison);

        Assertions.assertEquals(livraison.getDemandeLivraison(), livraison2.getDemandeLivraison());
        Assertions.assertEquals(livraison.getLivreur(), livraison2.getLivreur());
        Assertions.assertEquals(livraison.getHeure(), livraison2.getHeure());
        Assertions.assertEquals(livraison.isDansSaPlageHoraire(), livraison2.isDansSaPlageHoraire());
    }
}
