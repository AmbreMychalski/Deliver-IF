package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ComparateurHeureLivraisonTest {

    ComparateurHeureLivraison comparateurHeureLivraison = new ComparateurHeureLivraison();

    @Test
    @DisplayName("Test de la comparaison entre deux heures de livraison")
    void comparerTest() {
        String heure1 = "12h05";
        String heure2 = "12h55";
        String heure3 = "13h15";

        Assertions.assertEquals(50, comparateurHeureLivraison.compare(heure2, heure1));
        Assertions.assertEquals(1, comparateurHeureLivraison.compare(heure3, heure1));
    }
}
