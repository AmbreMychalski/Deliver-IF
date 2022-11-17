package modele;

import exception.FichierNonConformeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JourneeTest {

    void chargerDemandesLivraisonTest() {

    }

    void annulerDemandesLivraisonTest() {

    }

    void ajouterDemandeLivraisonTest() {


    }

    void sauvegarderDemandesLivraisonTest() {

    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void calculerTourneeTest() throws FichierNonConformeException {
        // Chargement du plan

        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        List<Segment> listeSegments1 = new ArrayList<>();
        Intersection intersection1 = new Intersection(1L, 0F, 1F);
        Intersection intersection2 = new Intersection(1L, 1F, 2F);

        // Définition de la tournée
        Trajet trajet1 = new Trajet(listeSegments1, 12, intersection1, intersection2);
    }
}
