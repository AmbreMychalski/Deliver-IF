package test.modele;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import exception.FichierNonConformeException;
import modele.Plan;

public class PlanTests {
    
    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() throws Exception {
        
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);
        
        Assertions.assertEquals("Rue Danton", plan.getSegments().get(0).getNom());
    }
    
    @Test
    void exceptionFichierNullTest() {        
        Assertions.assertThrows(Exception.class, () -> {
            new Plan(null);
        });
    }
    
    @Test
    void FichierNonConformeTest() {
        File fichierPlan = new File("data/testExceptionPlan.xml");
        Assertions.assertThrows(FichierNonConformeException.class,() -> {
            new Plan(fichierPlan);
        });
    }
        
    void exceptionFichierNonConformeTest() {

        File fichierPlan = new File("data/testExceptionPlan.xml");
        
        Assertions.assertThrows(Exception.class, () -> {
            new Plan(fichierPlan);
        });
    }
}
