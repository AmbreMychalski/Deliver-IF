package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ComparateurPlageHoraireTest {

    PlageHoraireComparator comparateurPlageHoraire = new PlageHoraireComparator();
    PlageHoraire plageHoraire1 = new PlageHoraire(9, 10);
    PlageHoraire plageHoraire2 = new PlageHoraire(10, 11);
    PlageHoraire plageHoraire3 = new PlageHoraire(11, 12);

    @Test
    @DisplayName("Test de la comparaison des plages horaires")
    void comparerTest() {
        Assertions.assertEquals(0,  comparateurPlageHoraire.compare(plageHoraire1, plageHoraire1));
        Assertions.assertEquals(-1,  comparateurPlageHoraire.compare(plageHoraire1, plageHoraire2));
        Assertions.assertEquals(1,  comparateurPlageHoraire.compare(plageHoraire3, plageHoraire2));
    }
}
