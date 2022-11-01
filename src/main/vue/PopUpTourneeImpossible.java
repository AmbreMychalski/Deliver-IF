package vue;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import controleur.ControleurFenetrePrincipale;

public class PopUpTourneeImpossible {
    public static void display (ControleurFenetrePrincipale c) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Tournee impossible");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("La tournee crée ne peux pas prendre en compte toutes les demandes de livraison");

        Button nouveauLivreur = new Button ("Assigner nouveau livreur");
        nouveauLivreur.setOnAction(e -> window.close());

        Button supprimerLivraison = new Button ("Supprimer livraison");

        supprimerLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                c.actionPopUpsupprimerLivraison(e);
                window.close();
            }
        });

        //supprimerLivraison.setOnAction(e -> c.actionPopUpsupprimerLivraison(e));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, nouveauLivreur, supprimerLivraison);

        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
}
