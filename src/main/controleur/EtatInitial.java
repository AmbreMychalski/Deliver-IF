package controleur;

import java.io.File;

import exception.FichierNonConformeException;
import javafx.stage.FileChooser;
import modele.Plan;
import modele.Segment;

public class EtatInitial extends Etat{
    public void chargerPlan(ControleurFenetrePrincipale c) throws FichierNonConformeException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger un plan");
        File fichier = fileChooser.showOpenDialog(c.stage);
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        try {
            c.planCharge = new Plan(fichier);
            c.journee.setPlan(c.planCharge);

            c.latMax = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLatitude())
                    .max((a, b) -> Float.compare(a, b)).orElse(0f);
            c.latMin = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLatitude())
                    .min((a, b) -> Float.compare(a, b)).orElse(0f);
            c.longMax = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLongitude())
                    .max((a, b) -> Float.compare(a, b)).orElse(0f);
            c.longMin = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLongitude())
                    .min((a, b) -> Float.compare(a, b)).orElse(0f);

            c.largeurPlan = c.longMax - c.longMin;
            c.hauteurPlan = c.latMax - c.latMin;

            c.echelle = Math.min(c.canvasPlan.getWidth() / c.largeurPlan,
                    c.canvasPlan.getHeight() / c.hauteurPlan);

            for (Segment segment : c.planCharge.getSegments()) {
                c.dessinerSegmentLatLong(segment.getOrigine().getLatitude(),
                        segment.getOrigine().getLongitude(),
                        segment.getDestination().getLatitude(),
                        segment.getDestination().getLongitude(),
                        c.COULEUR_SEGMENT);
            }

            c.dessinerIntersectionLatLong(c.canvasPlan.getGraphicsContext2D(),
                    c.planCharge.getEntrepot().getLatitude(),
                    c.planCharge.getEntrepot().getLongitude(),
                    c.COULEUR_DEPOT,
                    c.TAILLE_CERCLE_INTERSECTION,
                    true,
                    "Cercle");
            c.titledPaneEditionDemande.setVisible(true);
            c.titlePaneSelectionDemande.setVisible(true);
            c.buttonChargerDemandes.setDisable(false);
            c.buttonAutoriserAjouterLivraison.setDisable(false);
            c.etatCourant = c.etatSansDemande;
        } catch (Exception ex){
            throw  new FichierNonConformeException("Le fichier comporte des problèmes");
        }


    }
}
