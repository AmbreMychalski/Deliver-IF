package tests.modele;

import java.io.File;

import org.junit.jupiter.api.Test;

import main.modele.Journee;
import main.modele.Plan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.Assert;

public class PlanTest {
    
    @Test
    @DisplayName("Test de l'analyse XML")
    void analyserXMLTest() {
        
        File fichierPlan = new File("data\\testPlan.xml");
        
        // On charge le plan
        
        Plan plan = new Plan(fichierPlan);
        
        // On vérifie la première intersection
        
        Assert.assertEquals("Rue Gérard", plan.getSegments().get(0).getNom());
    }
    
    @Test
    void exceptionTest() {
        
    }
}
