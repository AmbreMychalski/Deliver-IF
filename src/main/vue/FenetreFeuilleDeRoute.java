package vue;

import controleur.ControleurFenetrePrincipale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.TourneeSerialisation;

import java.awt.*;
import java.io.File;

public class FenetreFeuilleDeRoute {
    public static void display (ControleurFenetrePrincipale c, int livreur) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        String tournee = null;
        TourneeSerialisation serialisation = new TourneeSerialisation(c.getJournee().getTournees(), c.getPlanCharge());;
        try{
            tournee= serialisation.serialiser(livreur);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        window.setTitle("Feuille de route");
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.setMaxHeight(1000);
        ScrollPane scrollPane = new ScrollPane();
        Label label = new Label();

        label.setText(tournee);
        scrollPane.setContent(label);
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox layout = new VBox(10);
        Button sauvegarderFeuilleDeRoute = new Button("Sauvegarder la feuille de route");
        sauvegarderFeuilleDeRoute.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File(".\\data"));
                fileChooser.setTitle("Sauvegarder la feuille de route");
                try{
                    File fichier = fileChooser.showSaveDialog(window);
                    ControleurFenetrePrincipale.logger.info("Sauvegarde à l'emplacement "
                            + fichier.getAbsolutePath());
                    serialisation.sauvegarderDansFichier(fichier);
                } catch (Exception ex){

                    ControleurFenetrePrincipale.logger
                            .error("Erreur lors de la sauvegarde de la feuille de route");
                }
            }
        });
        layout.getChildren().addAll(sauvegarderFeuilleDeRoute, scrollPane);

        layout.setAlignment(Pos.CENTER);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Scene scene = new Scene(layout, width-500, height-300);
        window.setScene(scene);
        window.show();

    }
}
