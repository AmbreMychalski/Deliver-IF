package controleur;

import java.io.File;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import modele.Intersection;
import modele.Plan;
import modele.Segment;
import vue.VueFenetrePrincipale;

public class EtatInitial extends Etat{
    public EtatInitial() {
        super.message = "Chargez un plan";
    }
    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger un plan");
        File fichier = fileChooser.showOpenDialog(c.vue.getStage());
        if(fichier == null){
            throw new Exception("Aucun fichier choisi");
        }
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        try {
            GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
            gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());

            c.planCharge = new Plan(fichier);
            c.journee.setPlan(c.planCharge);

            c.vue.latMax = c.planCharge.getIntersections().values().stream()
                    .map(Intersection::getLatitude)
                    .max(Float::compare).orElse(0f);
            c.vue.latMin = c.planCharge.getIntersections().values().stream()
                    .map(Intersection::getLatitude)
                    .min(Float::compare).orElse(0f);
            c.vue.longMax = c.planCharge.getIntersections().values().stream()
                    .map(Intersection::getLongitude)
                    .max(Float::compare).orElse(0f);
            c.vue.longMin = c.planCharge.getIntersections().values().stream()
                    .map(Intersection::getLongitude)
                    .min(Float::compare).orElse(0f);

            c.vue.largeurPlan = c.vue.longMax - c.vue.longMin;
            c.vue.hauteurPlan = c.vue.latMax - c.vue.latMin;

            /*c.vue.echelle = Math.min(c.vue.canvasPlan.getWidth() / c.vue.largeurPlan,
                    c.vue.canvasPlan.getHeight() / c.vue.hauteurPlan);*/
            c.vue.echelleLong = c.vue.canvasPlan.getWidth() / c.vue.largeurPlan;
            c.vue.echelleLat = c.vue.canvasPlan.getHeight() / c.vue.hauteurPlan;

            for (Segment segment : c.planCharge.getSegments()) {
                c.vue.dessinerSegment(segment,
                        c.vue.COULEUR_SEGMENT);
            }

            c.vue.dessinerIntersection(c.vue.canvasPlan.getGraphicsContext2D(),
                    c.getJournee().getPlan().getEntrepot(),
                    c.vue.COULEUR_DEPOT,
                    c.vue.TAILLE_CERCLE_INTERSECTION,
                    true,
                    VueFenetrePrincipale.FormeIntersection.CERCLE);
            c.vue.titledPaneEditionDemande.setVisible(true);
            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.changementEtat(c.etatSansDemande);
        } catch (Exception ex){
            throw  new FichierNonConformeException("Le fichier comporte des problèmes");
        }


    }
}
