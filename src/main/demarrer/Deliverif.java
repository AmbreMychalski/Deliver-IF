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
public class Deliverif {

    /**
     * Lance l'application.
     * @param args
     */
	public static void main(String[] args) {
		Application.launch(LanceurApplication.class, args);
	}

}
