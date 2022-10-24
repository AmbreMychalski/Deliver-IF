package demarrer;


import controleur.ControleurFenetrePrincipale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanceurApplication extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FenetrePrincipale.fxml"));
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
