package modele;

import exception.FichierNonConformeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanTest {

    public static final Logger logger = LogManager.getLogger(PlanTest.class);

    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        Intersection entrepot = new Intersection(0L, Float.valueOf(2), Float.valueOf(2));

        Assertions.assertEquals(entrepot, plan.getEntrepot());
        Assertions.assertEquals("Rue Danton", plan.getSegments().get(0).getNom());
    }
    
    @Test
    @DisplayName("Test de l'exception en cas de fichier null en entrée")
    void exceptionFichierNullTest() {        
        Assertions.assertThrows(Exception.class, () -> new Plan(null));
    }
    
    @Test
    @DisplayName("Test de l'exception en cas de problème lors de la lecture du fichier")
    void exceptionFichierNonConformeTest() {
        File fichierPlan = new File("data/testExceptionPlan.xml");

        Assertions.assertThrows(FichierNonConformeException.class,() -> new Plan(fichierPlan));
    }

    @Test
    @DisplayName("Test de la méthode : estLivrable")
    void estLivrableTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        logger.error("Valeurs : " + plan.getIntersections().get(Long.valueOf(7)));

        Assertions.assertTrue(plan.estLivrable(plan.getIntersections().get(Long.valueOf(1))));
        Assertions.assertFalse(plan.estLivrable(plan.getIntersections().get(Long.valueOf(7))));
        Assertions.assertFalse(plan.estLivrable(plan.getIntersections().get(Long.valueOf(8))));
    }

    @Test
    @DisplayName("Test de la méthode : calculerPlusCourtChemin (entre deux intersections)")
    void calculerPlusCourtCheminTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        Intersection intersection0 = plan.getIntersections().get(Long.valueOf(0));
        Intersection intersection4 = plan.getIntersections().get(Long.valueOf(4));

        List<Segment> listeSegments = new ArrayList<>();

        listeSegments.add(plan.getSegments().get(1));
        listeSegments.add(plan.getSegments().get(3));
        listeSegments.add(plan.getSegments().get(4));
        listeSegments.add(plan.getSegments().get(7));

        Assertions.assertEquals(plan.calculerPlusCourtChemin(intersection0, intersection4), listeSegments);
    }

    @Test
    @DisplayName("Test de la méthode : calculerPlusCourtsChemins (de toutes les intersections, depuis un départ)")
    void calculerPlusCourtsCheminsTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        List<Intersection> listeIntersections = new ArrayList<>();
        listeIntersections.add(plan.getIntersections().get(Long.valueOf(0)));
        listeIntersections.add(plan.getIntersections().get(Long.valueOf(3)));
        listeIntersections.add(plan.getIntersections().get(Long.valueOf(7)));

        Intersection depart = plan.getIntersections().get(Long.valueOf(5));

        HashMap<Intersection, Float> hashMap = new HashMap<>();
        hashMap.put(listeIntersections.get(0), Float.valueOf(16));
        hashMap.put(listeIntersections.get(1), Float.valueOf(15));
        hashMap.put(listeIntersections.get(2), Float.valueOf(19));

        Assertions.assertEquals(hashMap, plan.calculerPlusCourtsChemins(listeIntersections, depart));
    }

    @Test
    @DisplayName("Test de la méthode : obtenirIntersectionLaPlusProche")
    void obtenirIntersectionLaPlusProcheTest() {

    }

/*    @Test
    @DisplayName("Test de la méthode : sontConnectees")
    void sontConnecteesTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        Intersection depart1 = plan.getIntersections().get(Long.valueOf("7"));
        Intersection depart2 = plan.getIntersections().get(Long.valueOf("5"));
        Intersection arrivee = plan.getIntersections().get(Long.valueOf("0"));

        Assertions.assertTrue(plan.sontConnectee(depart2, arrivee));
        Assertions.assertTrue(plan.sontConnectee(depart2, arrivee));

    }*/

    @Test
    @DisplayName("Test de la méthode : calculHeuristique")
    void calculHeuristiqueTest() {

    }

    @Test
    @DisplayName("Test de la méthode : obtenirRuesIntersection")
    void obtenirRuesIntersection() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);

        Intersection intersection4 = plan.getIntersections().get(Long.valueOf(4));

        List<String> listeRues = new ArrayList<>();

        // On ne prend que les deux premières rues de la liste
        listeRues.add("Rue Rousseau");
        listeRues.add("Rue Bellecour");

        Assertions.assertEquals(listeRues, plan.obtenirRuesIntersection(intersection4));
    }
}
