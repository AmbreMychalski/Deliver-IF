package test.modele;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.modele.Plan;

public class PlanTests {
    
    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() throws Exception {
        
        File fichierPlan = new File("data\\testPlan.xml");
        
        // On charge le plan
        
        Plan plan = new Plan(fichierPlan);
        
        // On vérifie la première intersection
        
        Assertions.assertEquals("Rue Danton", plan.getSegments().get(0).getNom());
    }
    
    @Test
    void exceptionFichierNullTest() {        
        Assertions.assertThrows(Exception.class, () -> {
            new Plan(null);
        });
    }
    
    @Test
    void exceptionFichierNonConformeTest() {

        File fichierPlan = new File("data/testExceptionPlan.xml");
        
        Assertions.assertThrows(Exception.class, () -> {
            new Plan(fichierPlan);
        });
    }
}