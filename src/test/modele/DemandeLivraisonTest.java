package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DemandeLivraisonTest {

    Intersection intersection1                 = new Intersection(1L,20, 20);
    Intersection intersection2                 = new Intersection(5L,21, 21);
    PlageHoraire plageHoraire1                 = new PlageHoraire(10, 11);
    PlageHoraire plageHoraire2                 = new PlageHoraire(11, 12);
    DemandeLivraison demandeLivraison1         = new DemandeLivraison(intersection1, plageHoraire1);
    DemandeLivraison demandeLivraison2         = new DemandeLivraison(intersection2, plageHoraire1);
    DemandeLivraison demandeLivraisonAModifier = new DemandeLivraison(intersection2, plageHoraire1);

    @Test
    @DisplayName("Test de la modification d'une demande de livraison")
    void modifierDemandeLivraisonTest() {
        demandeLivraisonAModifier.modifierDemandeLivraison(intersection1, plageHoraire1);
        Assertions.assertTrue(demandeLivraisonAModifier.getIntersection() == intersection1);

        demandeLivraisonAModifier.modifierDemandeLivraison(intersection1, plageHoraire2);
        Assertions.assertTrue(demandeLivraisonAModifier.getPlageHoraire() == plageHoraire2);

    }

    @Test
    @DisplayName("Test de la récupération de l'ID de l'intersection")
    void getIdIntersectionTest() {
        Assertions.assertEquals(1L, demandeLivraison1.getIdIntersection());
    }

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
