package controleur;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import modele.*;

import java.io.File;
import java.util.ArrayList;

import static controleur.ControleurFenetrePrincipale.LOGGER;

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

            if(fichier == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            c.vue.labelGuideUtilisateur.setText("Aucun fichier sélectionné. Veuillez réessayer.");
            throw new FichierNonConformeException("Problème lors du choix du fichier.");
        }

        LOGGER.info("Fichier choisi = " + fichier.getAbsolutePath());

        try {
            GraphicsContext gc = c.vue.canvasPlan.getGraphicsContext2D();
            GraphicsContext gcTrajet = c.vue.canvasPlanTrajet.getGraphicsContext2D();
            GraphicsContext gcIntersection = c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D();
            gc.clearRect(0, 0, c.vue.canvasPlan.getWidth(), c.vue.canvasPlan.getHeight());
            gcTrajet.clearRect(0, 0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
            gcIntersection.clearRect(0, 0, c.vue.canvasIntersectionsLivraisons.getWidth(), c.vue.canvasIntersectionsLivraisons.getHeight());
            c.vue.textfieldIdentifiantIntersectionSelection.setText("");
            c.vue.textfieldPlageHoraire.setText("");
            this.resetLabelRuesIntersection(c);
            if(c.journee.getLivreurs().get(0).getDemandeLivraisons().size() !=0){
                c.journee = new Journee();
                c.journee.ajouterObservateur(c.vue);
                Livreur.reinitializeNbLivreurs();
                Livreur liv = new Livreur();
                liv.ajouterObservateur(c.vue);
                c.journee.getLivreurs().add(liv);
                c.etatCourant.majComboboxLivreur(c);
                c.vue.comboboxLivreur.getSelectionModel().selectFirst();
            }
            Plan plan;

            try {
                plan = new Plan(fichier);
            } catch(Exception e) {
                c.vue.labelGuideUtilisateur.setText("Problème lors de la lecture du fichier.");
                throw new FichierNonConformeException("Problème lors de la lecture du fichier.");
            }

            c.journee.setPlan(plan);

            Command com = new PlanCommand (c);
            com.undoCommand();
            com.doCommand();

            c.vue.titledPaneEditionDemande.setVisible(true);
            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.vue.buttonReinitAffPlan.setVisible(true);
            c.changementEtat(c.etatSansDemande);
        } catch (Exception ex) {
            c.vue.labelGuideUtilisateur.setText("Problème lors de la lecture du fichier.");
        }
    }
}
