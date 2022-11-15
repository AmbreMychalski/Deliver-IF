package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IntersectionTest {

    Intersection intersection = new Intersection(1L,20, 20);

    @Test
    @DisplayName("Test des valeurs de retour du test d'égalité de la classe Intersection")
    void isEgalTest() {
        Assertions.assertNotEquals(null, intersection);
        Assertions.assertEquals(intersection, intersection);

        Plan plan = new Plan();

        Assertions.assertNotEquals(intersection, plan);
    }
}
