package vue;

import controleur.ControleurFenetrePrincipale;
import vue.VueFenetrePrincipale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanceurApplication extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

	        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FenetrePrincipale.fxml"));
	
	        Scene scene = new Scene(loader.load());
	        primaryStage.setTitle("Calcul de tournées");
	
	        VueFenetrePrincipale controller = loader.getController();
	
	        controller.setStage(primaryStage);
	
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
