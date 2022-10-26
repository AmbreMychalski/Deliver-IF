/*package test.modele;

import java.io.File;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.modele.Plan;

public class PlanTests {
    
    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() {
        
        File fichierPlan = new File("data\\testPlan.xml");
        
        // On charge le plan
        
        Plan plan = new Plan(fichierPlan);
        
        // On vérifie la première intersection
        
        Assert.assertEquals("Rue Emile Zola", plan.getSegments().get(2).getNom());
        Assert.assertEquals(2, plan.getSegments().get(2).getLongueur(),0.0);
       // Assert.assertEquals(2, plan.getSegments().get(2).getOrigine().getIdIntersection());
        Assert.assertEquals(5, plan.getSegments().get(2).getOrigine().getLatitude(),0.0);
        Assert.assertEquals(10, plan.getSegments().get(2).getOrigine().getLongitude(),0.0);
        
       // Assert.assertEquals(1, plan.getSegments().get(2).getDestination().getIdIntersection());
        Assert.assertEquals(3, plan.getSegments().get(2).getDestination().getLatitude(),0.0);
        Assert.assertEquals(8, plan.getSegments().get(2).getDestination().getLongitude(),0.0);
    }
    
    @Test
    void exceptionTest() {
        
    }
}*/
