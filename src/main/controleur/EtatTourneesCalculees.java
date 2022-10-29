package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;

public class EtatTourneesCalculees extends Etat{

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c){
        this.sauvegarderListeDemandes(c);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        if (c.planCharge != null) {
            ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=("
                    + event.getX() + "," + event.getY() + ") (lat,long)="
                    + c.convertirYEnLatitude(event.getY()) + ","
                    + c.convertirXEnLongitude(event.getX()));
            Intersection intersectionTrouvee =
                    c.trouverIntersectionCoordoneesPixels(event.getX(),
                            event.getY());
            //ControleurFenetrePrincipale.logger.debug("Intersection trouvée = " + intersectionTrouvee);
            if (intersectionTrouvee != null) {
                c.textfieldIdentifiantIntersection.setText(
                        intersectionTrouvee.getIdIntersection().toString());


                GraphicsContext gc = c.canvasInterieurPlan.getGraphicsContext2D();
                gc.clearRect(0, 0, c.canvasInterieurPlan.getWidth(), c.canvasInterieurPlan.getHeight());
                c.dessinerIntersectionLatLong(gc,
                        intersectionTrouvee.getLatitude(),
                        intersectionTrouvee.getLongitude(),
                        Color.DARKORCHID,
                        c.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
                        true,
                        "Cercle");

            } else {
                c.textfieldIdentifiantIntersection.setText("");
            }
        } else {
            ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=("
                    + event.getX() + "," + event.getY() + ")");
        }

    }

    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.tableViewDemandesLivraison.setDisable(true);
        c.etatCourant = c.etatSaisieNouvelleDemandeAvecTournees;
    }
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeAvecTournees;
    }

    
}
