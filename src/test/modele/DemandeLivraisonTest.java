package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DemandeLivraisonTest {

    Intersection intersection1 = new Intersection(1L,20, 20);
    Intersection intersection2 = new Intersection(5L,21, 21);
    PlageHoraire plageHoraire = new PlageHoraire(10, 11);
    DemandeLivraison demandeLivraison1 = new DemandeLivraison(intersection1, plageHoraire);
    DemandeLivraison demandeLivraison2 = new DemandeLivraison(intersection2, plageHoraire);

    @Test
    @DisplayName("Test de l'égalité entre deux demandes de livraison")
    void isEgalTest() {
        Plan plan = new Plan();

        Assertions.assertFalse(demandeLivraison1.equals(null));
        Assertions.assertTrue(demandeLivraison1.equals(demandeLivraison1));
        Assertions.assertFalse(demandeLivraison1.equals(plan));
        Assertions.assertFalse(demandeLivraison1.equals(demandeLivraison2));
    }
}
