package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;

public class EtatModifierDemandeLivraisonAvecTournees extends Etat {

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
        DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.dessinerIntersectionLatLong(c.canvasInterieurPlan.getGraphicsContext2D(),
                    ligne.getIntersection().getLatitude(),
                    ligne.getIntersection().getLongitude(),
                    c.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    "Rectangle");
        }
    }

    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.effectuerModification(c);
        this.calculerEtAfficherTournee(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeAvecTournees;
    }
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerModification(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeAvecTournees;
    }

    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}
}
