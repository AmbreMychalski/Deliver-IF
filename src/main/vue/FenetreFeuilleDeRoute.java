package vue;

import controleur.ControleurFenetrePrincipale;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Livreur;
import modele.TourneeSerialisation;

import java.awt.*;
import java.io.File;

import static controleur.ControleurFenetrePrincipale.LOGGER;

/**
 * Fenêtre spécifique à l'affichage de la feuille de route
 */
public class FenetreFeuilleDeRoute {
    public static void display (ControleurFenetrePrincipale c, Livreur livreur) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        TourneeSerialisation serialisation = new TourneeSerialisation(c.getJournee().getPlan());
        String               tournee = serialisation.serialiser(livreur);
        ScrollPane           scrollPane = new ScrollPane();
        Label                label = new Label();

        window.setTitle("Feuille de route");
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.setMaxHeight(600);

        label.setText(tournee);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        scrollPane.setContent(label);
        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox layout = new VBox(10);
        Button sauvegarderFeuilleDeRoute = new Button("Sauvegarder la feuille de route");
        sauvegarderFeuilleDeRoute.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder la feuille de route");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichier TXT", "*.txt", "*.TXT"));

            try {
                File fichier = fileChooser.showSaveDialog(window);

                LOGGER.info("Sauvegarde à l'emplacement " + fichier.getAbsolutePath());
                serialisation.sauvegarderDansFichier(fichier);
            } catch (Exception ex) {
                LOGGER.error("Erreur lors de la sauvegarde de la feuille de route");
            }
        });

        layout.getChildren().addAll(sauvegarderFeuilleDeRoute, scrollPane);
        layout.setAlignment(Pos.CENTER);

        Dimension   size = Toolkit.getDefaultToolkit().getScreenSize();
        int         width = (int)size.getWidth();
        int         height = (int)size.getHeight();
        Scene       scene = new Scene(layout, (0.35*width), (0.5*height));

        window.setScene(scene);
        window.show();
    }
}
