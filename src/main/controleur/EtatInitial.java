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
        File fichier = fileChooser.showOpenDialog(c.vue.getStage());
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        try {
            c.planCharge = new Plan(fichier);
            c.journee.setPlan(c.planCharge);

            c.vue.latMax = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLatitude())
                    .max((a, b) -> Float.compare(a, b)).orElse(0f);
            c.vue.latMin = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLatitude())
                    .min((a, b) -> Float.compare(a, b)).orElse(0f);
            c.vue.longMax = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLongitude())
                    .max((a, b) -> Float.compare(a, b)).orElse(0f);
            c.vue.longMin = c.planCharge.getIntersections().values().stream()
                    .map(intersection -> intersection.getLongitude())
                    .min((a, b) -> Float.compare(a, b)).orElse(0f);

            c.vue.largeurPlan = c.vue.longMax - c.vue.longMin;
            c.vue.hauteurPlan = c.vue.latMax - c.vue.latMin;

            c.vue.echelle = Math.min(c.vue.canvasPlan.getWidth() / c.vue.largeurPlan,
                    c.vue.canvasPlan.getHeight() / c.vue.hauteurPlan);

            for (Segment segment : c.planCharge.getSegments()) {
                c.vue.dessinerSegment(segment,
                        c.vue.COULEUR_SEGMENT);
            }

            c.vue.dessinerIntersection(c.vue.canvasPlan.getGraphicsContext2D(),
                    c.getJournee().getPlan().getEntrepot(),
                    c.vue.COULEUR_DEPOT,
                    c.vue.TAILLE_CERCLE_INTERSECTION,
                    true,
                    "Cercle");
            c.vue.titledPaneEditionDemande.setVisible(true);
            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.vue.buttonChargerDemandes.setDisable(false);
            c.vue.buttonAutoriserAjouterLivraison.setDisable(false);
            c.etatCourant = c.etatSansDemande;
        } catch (Exception ex){
            throw  new FichierNonConformeException("Le fichier comporte des problèmes");
        }


    }
}
