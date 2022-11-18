package modele;

import exception.FichierNonConformeException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JourneeTest {
    Journee journee = new Journee();
    List<Livreur> listeLivreurs = new ArrayList<>();
    Livreur livreur1 = new Livreur();
    Livreur livreur2 = new Livreur();
    File filePlan = new File("data\\largeMap.xml");
    File file1 = new File("data\\testDemandesLivraison.xml");
    File file2 = new File("data\\testDemandesLivraisonImpossibles.xml");
    File file3 = new File("data\\testTSPNonTrouve.xml");
    File file4 = new File("data\\testDemandeUnique.xml");
    Plan plan;

    @Test
    @DisplayName("Test du chargement des demandes de livraison")
    void chargerDemandesLivraisonTest() throws Exception {
        plan = new Plan(filePlan);
        journee.setPlan(plan);

        listeLivreurs.add(livreur1);
        listeLivreurs.add(livreur2);
        journee.setLivreurs(listeLivreurs);

        journee.chargerDemandesLivraison(file1, livreur1);

        Intersection intersection = new Intersection(479185301L, 45.750896F, 4.859119F);
        PlageHoraire plageHoraire = new PlageHoraire(11, 12);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);

        Assertions.assertEquals(demandeLivraison.getIntersection(), journee.getLivreurs().get(0).getDemandeLivraisons().get(0).getIntersection());
        Assertions.assertThrows(Exception.class, () -> journee.chargerDemandesLivraison(file2, livreur1));
    }

    void annulerDemandesLivraisonTest() {

    }

    void ajouterDemandeLivraisonTest() {

    }

    @Test
    @DisplayName("Test de la sauvegarde des demandes de livraison")
    void sauvegarderDemandesLivraisonTest() throws Exception {
        File fichierSauvegarde = new File("data\\sauvegarde.xml");

        plan = new Plan(filePlan);
        journee.setPlan(plan);

        listeLivreurs.add(livreur1);
        listeLivreurs.add(livreur2);
        journee.setLivreurs(listeLivreurs);

        journee.chargerDemandesLivraison(file1, livreur1);
        journee.sauvegarderDemandesLivraison(fichierSauvegarde, livreur1);

        Assertions.assertEquals(fichierSauvegarde.length(), file1.length());
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void calculerTourneeTest() throws Exception {
        // Cas où tout se passe bien
        Plan plan = new Plan(filePlan);

        journee.setPlan(plan);
        listeLivreurs.add(livreur1);
        journee.setLivreurs(listeLivreurs);
        journee.chargerDemandesLivraison(file1, livreur1);
        journee.calculerTournee(livreur1);

        Assertions.assertTrue(journee.calculerTournee(livreur1));

        // Cas où le TSP ne trouve pas de solution
        plan = new Plan(filePlan);

        journee.setPlan(plan);
        journee.chargerDemandesLivraison(file3, livreur1);

        Assertions.assertFalse(journee.calculerTournee(livreur1));
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void ajouterLivreurTest() {
        listeLivreurs.add(livreur1);
        listeLivreurs.add(livreur2);
        journee.setLivreurs(listeLivreurs);
        journee.setPlan(plan);

        Assertions.assertSame(journee.getLivreurs().get(1), livreur2);
        Assertions.assertEquals(2, journee.getLivreurs().size());
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void modifierDemandeLivraisonTest() throws Exception {
        File fichierPlan = new File("data\\testPlan.xml");
        Plan plan = new Plan(filePlan);
        Intersection intersection = new Intersection(479185301L, 45.750896F, 4.859119F);
        PlageHoraire plageHoraire = new PlageHoraire(9, 10);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);

        journee.setPlan(plan);
        listeLivreurs.add(livreur1);
        journee.setLivreurs(listeLivreurs);
        journee.chargerDemandesLivraison(file1, livreur1);
        journee.modifierDemandeLivraison(livreur1, livreur1.getDemandeLivraisons().get(3), intersection, plageHoraire);

        Assertions.assertEquals(livreur1.getDemandeLivraisons().get(3), demandeLivraison);
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void ajouterDemandeLivraisonTourneeTest() {

    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void assignerLivraisonNouveauLivreurTest() {

    }

    @Test
    @DisplayName("Test de la méthode d'ajout d'une livraison dans une tournée"
            + " déjà calculée")
    void ajouterLivraisonTourneeTest() throws Exception {
        Plan plan = new Plan(filePlan);
        Intersection intersection = new Intersection(25319193L, 45.74265F, 4.8790674F);
        PlageHoraire plageHoraire = new PlageHoraire(9, 10);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);
        Livraison livraisonAAjouter = new Livraison(demandeLivraison, 9F, livreur1, true);
        Livraison livraisonPrecedente;

        journee.setPlan(plan);
        listeLivreurs.add(livreur1);
        journee.setLivreurs(listeLivreurs);
        journee.chargerDemandesLivraison(file1, livreur1);
        journee.calculerTournee(livreur1);

        // Cas où la livraison précédente est en plein milieu
        livraisonPrecedente = journee.getLivreurs().get(0).getTournee().getLivraisons().get(1);

        journee.ajouterLivraisonTournee(livraisonAAjouter, livraisonPrecedente, livreur1);

        Assertions.assertEquals(livraisonAAjouter.getIdIntersectionLivraison(),
                journee.getLivreurs().get(0).getTournee().getTrajets().get(2).arrivee.getIdIntersection());

        // Cas où la livraison précédente est au tout début
        journee.supprimerLivraisonTournee(livreur1, livraisonAAjouter);
        journee.ajouterLivraisonTournee(livraisonAAjouter, null, livreur1);

        Assertions.assertEquals(livraisonAAjouter.getIdIntersectionLivraison(),
                journee.getLivreurs().get(0).getTournee().getTrajets().get(0).arrivee.getIdIntersection());

        journee.supprimerLivraisonTournee(livreur1, livraisonAAjouter);
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void supprimerLivraisonTourneeTest() throws Exception {
        Plan plan = new Plan(filePlan);
        Intersection intersection = new Intersection(25319193L, 45.74265F, 4.8790674F);
        PlageHoraire plageHoraire = new PlageHoraire(9, 10);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);
        Livraison livraisonAAjouter = new Livraison(demandeLivraison, 9F, livreur1, true);
        Livraison livraisonSuivante;

        journee.setPlan(plan);
        listeLivreurs.add(livreur1);
        journee.setLivreurs(listeLivreurs);
        journee.chargerDemandesLivraison(file1, livreur1);
        journee.calculerTournee(livreur1);

        // Cas où la livraison se trouve au milieu
        livraisonSuivante = livreur1.getTournee().getLivraisons().get(2);
        journee.supprimerLivraisonTournee(livreur1, livreur1.getTournee().getLivraisons().get(1));

        Assertions.assertEquals(livraisonSuivante,
                journee.getLivreurs().get(0).getTournee().getLivraisons().get(1));

        // Cas où la tournée ne contient qu'une livraison
        livreur1.supprimerTournee();
        journee.chargerDemandesLivraison(file4, livreur1);
        journee.calculerTournee(livreur1);

        journee.supprimerLivraisonTournee(livreur1, livreur1.getTournee().getLivraisons().get(0));

        Assertions.assertEquals(null, livreur1.getTournee());
    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void creerListTrajetTest() {

    }

    @Test
    @DisplayName("Test de la méthode calculerTournee")
    void majHeureLivraisonTest() {

    }

    @Test
    @DisplayName("Test de la méthode pour récupérer l'id de l'intersection d'une"
            + "livraison")
    void getIdIntersectionLivraisonTest() throws Exception {
        Intersection intersection = new Intersection(479185301L, 45.750896F, 4.859119F);
        PlageHoraire plageHoraire = new PlageHoraire(9, 10);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);

        Assertions.assertEquals(intersection.getIdIntersection(), demandeLivraison.getIdIntersection());
    }

    @Test
    @DisplayName("Test de la méthode pour récupérer la plage horaire d'une livraison")
    void getPlageHoraireTest() throws Exception {
        Intersection intersection = new Intersection(479185301L, 45.750896F, 4.859119F);
        PlageHoraire plageHoraire = new PlageHoraire(9, 10);
        DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);

        Assertions.assertEquals(plageHoraire, demandeLivraison.getPlageHoraire());
    }
}
