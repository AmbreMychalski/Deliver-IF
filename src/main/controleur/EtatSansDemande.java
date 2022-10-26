package main.controleur;

import java.io.File;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import main.modele.DemandeLivraison;
import main.modele.Intersection;

public class EtatSansDemande implements Etat{

    public void chargerPlan(ControleurFenetrePrincipale c) {
        c.etatInitial.chargerPlan(c);
    }
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.etatCourant = c.etatSaisieNouvelleDemandeSansTournees;
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
    
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}
    
    public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger des demandes de livraison");
        File fichier = fileChooser.showOpenDialog(c.stage);
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        c.journee.chargerDemandesLivraison(fichier);
        c.mettreAJourListeDemandes();
        c.etatCourant = c.etatAvecDemande;
    }
    
    public void supprimerDemande(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
    
    public void calculerTournees(ControleurFenetrePrincipale c) {}
    
    public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void fermerFenetre(ControleurFenetrePrincipale c) {}
    
    public void quitterLogiciel(ControleurFenetrePrincipale c) {}
    
    public void modifierDemande(ControleurFenetrePrincipale c) {}
}
