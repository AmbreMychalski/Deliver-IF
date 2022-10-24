package controleur;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControleurFenetrePrincipale {
	
	@FXML private Button buttonAjouterLivraison;
	@FXML private Button buttonCalculerTournees;
	@FXML private Button buttonSauvegarderDemandes;
	@FXML private Button buttonChargerDemandes;
	@FXML private Button buttonAfficherFeuillesRoute;
	@FXML private Button buttonChargerPlan;
	@FXML private TableView tableViewDemandesLivraison;
	@FXML private Canvas canvasPlan;
	@FXML private ComboBox comboboxPlageHoraire;
	@FXML private TextField textfieldIdentifiantIntersection;
	
	
	class Coordonnees {
		public float x;
		public float y;
	}
	
	@FXML 
	private void initialize() {
		buttonAjouterLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
		canvasPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
	}
	
	private void actionClicSurCanvas(MouseEvent event) {
		System.out.println("Clic sur le canvas, event = " + event);
	}

	private void actionBoutonAjouterLivraison(Event event) {
		System.out.println("Clic sur le bouton Ajouter Livraison, event = " + event);
	}
	
	private Coordonnees convertirPixelsEnCoordonnees(Coordonnees pixels) {
		Coordonnees coord = new Coordonnees();
		coord.x = 0;
		coord.y = 0;
		return coord;
	}
	
	private Coordonnees convertirCoordonneesEnPixels(Coordonnees coords) {
		Coordonnees pixels = new Coordonnees();
		pixels.x = 0;
		pixels.y = 0;
		return pixels;
	}
}
