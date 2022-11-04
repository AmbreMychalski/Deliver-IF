package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;

public class EtatModifierDemandeLivraisonSansTournees extends Etat {
 
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
        DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.vue.dessinerIntersection(c.vue.canvasInterieurPlan.getGraphicsContext2D(),
                    ligne.getIntersection(),
                    c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    "Rectangle");
        }
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.effectuerModification(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerModification(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
    }
}
