package modele;

import javafx.scene.paint.Color;
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

        Assertions.assertNotEquals(null, plageHoraire1); // null
        Assertions.assertEquals(plageHoraire1, plageHoraire1); // self
        Assertions.assertNotEquals(plageHoraire1, plan); // obj de type différent

        Assertions.assertEquals(plageHoraire1, plageHoraire2);
        Assertions.assertNotEquals(plageHoraire1, plageHoraire3);
    }

    @Test
    @DisplayName("Test du constructeur, pour vérifier les couleurs")
    void plageHoraireTest() {
        PlageHoraire plageHoraire = new PlageHoraire(11, 12);

        Assertions.assertEquals(Color.DEEPPINK, plageHoraire.getCouleur());
    }

    @Test
    @DisplayName("Test du toString")
    void toStringTest() {
        PlageHoraire plageHoraire = new PlageHoraire(11, 12);
        Assertions.assertEquals("De 11h à 12h", plageHoraire.toString());
    }
}
