package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlageHoraireTest {

    PlageHoraire plageHoraire1 = new PlageHoraire(9, 10);
    PlageHoraire plageHoraire2 = new PlageHoraire(9, 10);
    PlageHoraire plageHoraire3 = new PlageHoraire(10, 11);

    @Test
    @DisplayName("Test de l'égalité entre deux plages horaire")
    void isEgalTest() {
        Plan plan = new Plan();

        Assertions.assertFalse(plageHoraire1.equals(null)); // null
        Assertions.assertTrue(plageHoraire1.equals(plageHoraire1)); // self
        Assertions.assertFalse(plageHoraire1.equals(plan)); // obj de type différent

        Assertions.assertTrue(plageHoraire1.equals(plageHoraire2));
        Assertions.assertFalse(plageHoraire1.equals(plageHoraire3));
    }
}
