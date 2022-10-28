package main.controleur;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.modele.DemandeLivraison;
import main.modele.Intersection;
import main.modele.PlageHoraire;
import main.modele.Segment;
import main.modele.Tournee;
import main.modele.Trajet;

public class EtatSaisieNouvelleDemandeAvecTournees extends Etat { 
    
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

    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        String champIdentifiant = c.textfieldIdentifiantIntersection.getText();
        PlageHoraire plageHoraire = c.comboboxPlageHoraire.getValue();
        if(!champIdentifiant.isEmpty() && plageHoraire != null) {
            Intersection intersection = c.journee.getPlan().getIntersections()
                    .get(Long.parseLong(champIdentifiant));
            DemandeLivraison demande = 
                    new DemandeLivraison(intersection, plageHoraire);
            c.journee.ajouterDemandeLivraison(demande);
            c.tableViewDemandesLivraison.getItems().add(demande);
            c.tableViewDemandesLivraison.refresh();
            c.mettreAJourCanvasDemande();
            c.buttonValiderLivraison.setDisable(true);
            c.buttonAnnulerLivraison.setDisable(true);
            c.comboboxPlageHoraire.setDisable(true);
            c.comboboxPlageHoraire.setValue(null);
            c.textfieldIdentifiantIntersection.setText("");
            c.etatCourant = c.etatAvecDemande;
        } else {
            ControleurFenetrePrincipale.logger.warn("Informations manquantes pour l'ajout de la demande");
        }
        
        c.journee.calculerTournee();
        GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
        gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
        
        Tournee tournee = c.journee.getTournees().get(c.journee.getTournees().size()-1);
        List<Trajet> trajets = tournee.getTrajets();
        
        for(Trajet trajet : trajets) {
            List<Segment> segments = trajet.getSegments(); 
            for(Segment segment : segments) {
                c.dessinerTrajetLatLong((double)segment.getOrigine().getLatitude(),
                        (double)segment.getOrigine().getLongitude(),
                        (double)segment.getDestination().getLatitude(),
                        (double)segment.getDestination().getLongitude());
            }
            
        }
        
        c.etatCourant = c.etatTourneesCalculees;
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
