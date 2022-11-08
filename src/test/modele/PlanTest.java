package test.modele;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import exception.FichierNonConformeException;
import modele.Plan;

public class PlanTest {
    
    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(fichierPlan);
        
        Assertions.assertEquals("Rue Danton", plan.getSegments().get(0).getNom());
    }
    
    @Test
    @DisplayName("Test de l'exception en cas de fichier null en entrée")
    void exceptionFichierNullTest() {        
        Assertions.assertThrows(Exception.class, () -> {
            new Plan(null);
        });
    }
    
    @Test
    @DisplayName("Test de l'exception en cas de problème lors de la lecture du fichier")
    void exceptionFichierNonConformeTest() {
        File fichierPlan = new File("data/testExceptionPlan.xml");

        Assertions.assertThrows(FichierNonConformeException.class,() -> {
            new Plan(fichierPlan);
        });
    }
}
