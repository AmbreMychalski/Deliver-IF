package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;

public class EtatSaisieNouvelleDemandeSansTournees extends Etat{
    
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

                for(DemandeLivraison d: c.journee.getDemandesLivraison()) {
                    c.dessinerIntersectionLatLong(gc,
                                                    d.getIntersection().getLatitude(), 
                                                    d.getIntersection().getLongitude(), 
                                                    c.COULEUR_POINT_LIVRAISON, 
                                                    c.TAILLE_RECT_PT_LIVRAISON, 
                                                    true, 
                                                    "Rectangle");
                }
                
            } else {
                c.textfieldIdentifiantIntersection.setText("");
            }
        } else {
            ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=(" 
                    + event.getX() + "," + event.getY() + ")");
        }

    }
    
    public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        String champIdentifiant = c.textfieldIdentifiantIntersection.getText();
        PlageHoraire plageHoraire = c.comboboxPlageHoraire.getValue();
        if(!champIdentifiant.isEmpty() && plageHoraire != null) {
            Intersection intersection = c.journee.getPlan().getIntersections()
                    .get(Long.parseLong(champIdentifiant));
            DemandeLivraison demande = 
                    new DemandeLivraison(intersection, plageHoraire);
            c.journee.ajouterDemandeLivraison(demande);
            c.mettreAJourCanvasDemande();
            c.tableViewDemandesLivraison.getItems().add(demande);
            c.tableViewDemandesLivraison.refresh();
            c.buttonValiderLivraison.setDisable(true);
            c.buttonAnnulerLivraison.setDisable(true);
            c.comboboxPlageHoraire.setDisable(true);
            c.comboboxPlageHoraire.setValue(null);
            c.textfieldIdentifiantIntersection.setText("");
            c.etatCourant = c.etatAvecDemande;
        } else {
            ControleurFenetrePrincipale.logger.warn("Informations manquantes pour l'ajout de la demande");
        }
    }
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(true);
        c.buttonAnnulerLivraison.setDisable(true);
        c.comboboxPlageHoraire.setDisable(true);
        c.textfieldIdentifiantIntersection.setText("");
        if (c.journee.getDemandesLivraison().size() == 0) {
            c.etatCourant = c.etatSansDemande;
        } else {
            c.etatCourant = c.etatAvecDemande;
        }
    }
    
}
