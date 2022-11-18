package modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivreurTest {

    Intersection intersection = new Intersection(1L, 0.1F, 0.2F);
    PlageHoraire plageHoraire = new PlageHoraire(11, 12);
    DemandeLivraison demandeLivraison = new DemandeLivraison(intersection, plageHoraire);
    Livreur livreur = new Livreur();

    @Test
    void testToString() {
        Livreur livreur = new Livreur();
        livreur.setNumero(12);

        Assertions.assertEquals("12", livreur.toString());
    }

    @Test
    void ajouterDemandeLivraison() {
        livreur.ajouterDemandeLivraison(demandeLivraison);

        Assertions.assertEquals(1, livreur.getDemandeLivraisons().size());
    }

    @Test
    void modifierTournee() {
    }

    @Test
    void supprimerTournee() {
    }

    @Test
    void supprimerDemandeLivraison() {
    }

    @Test
    void supprimerLivraison() {
    }

    @Test
    void ajouterLivraisonTournee() {
    }

    @Test
    void modifierDemandeLivraison() {
    }
}