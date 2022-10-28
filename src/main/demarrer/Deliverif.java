/*
 * Deliverif 
 * 
 * Version 1.0
 */

package main.demarrer;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controleur.ControleurFenetrePrincipale;

/**
 * Point d'entrée de l'applicaiton Deliverif.
 * @author H4113
 *
 */
public class Deliverif extends Application {

    /**
     * Lance l'application.
     * @param args
     */
	public static void main(String[] args) {
		Application.launch(Deliverif.class, args);
	}
	
	@Override
    public void start(Stage primaryStage) throws Exception {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/FenetrePrincipale.fxml"));
            Parent root = (Parent)loader.load();
            ControleurFenetrePrincipale controleur = loader.getController();
            controleur.setStage(primaryStage);
            primaryStage.setTitle("Calcul de tournées");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
