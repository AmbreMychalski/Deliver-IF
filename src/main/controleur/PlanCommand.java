package controleur;

import javafx.scene.canvas.GraphicsContext;
import modele.Intersection;
import modele.Segment;
import vue.VueFenetrePrincipale;

public class PlanCommand implements Command{
    private ControleurFenetrePrincipale c;

    public PlanCommand(ControleurFenetrePrincipale controleur){
        c = controleur;
    }
    public void undoCommand (){
        GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
        gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());
    }
    public void doCommand(){
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

    }
}
