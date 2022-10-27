package main.controleur;

import java.io.File;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import main.modele.DemandeLivraison;
import main.modele.Intersection;
import main.modele.Segment;
import main.modele.Tournee;
import main.modele.Trajet;

public class EtatAvecDemande implements Etat{

    public void chargerPlan(ControleurFenetrePrincipale c) {}
    
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
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.mettreAJourListeDemandes();
            c.dessinerIntersectionLatLong(c.canvasInterieurPlan.getGraphicsContext2D(),
                                        ligne.getIntersection().getLatitude(), 
                                        ligne.getIntersection().getLongitude(),
                                        c.COULEUR_POINT_LIVRAISON_SELECTIONNE, 
                                        c.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE, 
                                        true, 
                                        "Rectangle");
            
            c.textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
            c.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
        }
        c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
    }
    
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
        if (fichier != null) {
            System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

            c.journee.chargerDemandesLivraison(fichier);
            c.mettreAJourListeDemandes();
        } 
    }
    
    public void supprimerDemande(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.setTitle("Sauvegarder des demandes de livraison");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        File fichier = fileChooser.showSaveDialog(c.stage);
        
        if (fichier != null) {
            ControleurFenetrePrincipale.logger.info("Sauvegarde à l'emplacement " 
                    + fichier.getAbsolutePath());
            c.journee.sauvegarderDemandesLivraison(fichier);
        } else {
            ControleurFenetrePrincipale.logger
                .error("Erreur lors de la sauvegarde des demandes");
        }
    }
    
    public void calculerTournees(ControleurFenetrePrincipale c) {
        c.journee.calculerTournee();
        GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
        gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
        for (Tournee tournee: c.journee.getTournees()) {
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
        }
        c.etatCourant = c.etatTourneesCalculees;
    }
    
    public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void fermerFenetre(ControleurFenetrePrincipale c) {}
    
    public void quitterLogiciel(ControleurFenetrePrincipale c) {}
    
    public void modifierDemande(ControleurFenetrePrincipale c) {}
    
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}
}
