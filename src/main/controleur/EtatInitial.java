package controleur;

import java.io.File;

import exception.FichierNonConformeException;
import javafx.stage.FileChooser;
import modele.Plan;

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
//            GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
//            gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());
            c.planCharge = new Plan(fichier);
            c.journee.setPlan(c.planCharge);

            Command com = new PlanCommand (c);
            com.undoCommand();
            com.doCommand();

            c.vue.titledPaneEditionDemande.setVisible(true);
            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.changementEtat(c.etatSansDemande);
        } catch (Exception ex){
            throw  new FichierNonConformeException("Le fichier comporte des problèmes");
        }


    }
}
