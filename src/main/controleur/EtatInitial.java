package controleur;

import java.io.File;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import modele.Plan;

public class EtatInitial extends Etat {

    public EtatInitial() {
        super.message = "Chargez un plan";
    }
    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        File fichier;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML",
                                       "*.xml", "*.XML"));
        fileChooser.setTitle("Charger un plan");

        try {
            fichier = fileChooser.showOpenDialog(c.vue.getStage());
        } catch(Exception e) {
            c.vue.labelGuideUtilisateur.setText("Problème lors du chargement du fichier.");
            throw new FichierNonConformeException("Problème lors du choix du fichier.");
        }
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        try {
//            GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
//            gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());
            c.planCharge = new Plan(fichier);
            GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
            gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());

            try {
                c.planCharge = new Plan(fichier);
            } catch(Exception e) {
                c.vue.labelGuideUtilisateur.setText("Problème lors du chargement du fichier.");
                throw new FichierNonConformeException("Problème lors du chargement du fichier.");
            }

            c.journee.setPlan(c.planCharge);

            Command com = new PlanCommand (c);
            com.undoCommand();
            com.doCommand();

            c.vue.titledPaneEditionDemande.setVisible(true);
            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.changementEtat(c.etatSansDemande);
        } catch (Exception ex) {
            c.vue.labelGuideUtilisateur.setText("Problème lors de la lecture du fichier.");
            throw new FichierNonConformeException("Problème lors de la lecture du fichier.");
        }
    }
}
