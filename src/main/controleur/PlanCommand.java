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
    public void doCommand() {
        c.vue.dessinerPlan();
    }
}
