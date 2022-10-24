package controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modele.Intersection;
import modele.Journee;
import modele.Plan;
import modele.Segment;

public class ControleurFenetrePrincipale {
	
	final double TAILLE_CERCLE_INTERSECTION = 5;
	final Color COULEUR_DEPOT = Color.RED;
	
	Stage thisStage;
	float largeurPlan;
	float hauteurPlan;
	double echelle;
	
	// modèle 
	Journee journee;
	Plan planCharge;
	
	// objets FXML 
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
	private Float latMax;
	private Float latMin;
	private Float longMax;
	private Float longMin;
	
	
	class Coordonnees {
		public double x;
		public double y;
	}
	
	@FXML 
	private void initialize() {
		buttonAjouterLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
		canvasPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
		buttonChargerPlan.setOnAction(event -> actionBoutonChargerPlan(event));
	}
	
	private void actionBoutonChargerPlan(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		fileChooser.setTitle("Charger un plan");
		File fichier = fileChooser.showOpenDialog(this.thisStage);
		System.out.println("Fichier choisi = " + fichier.getAbsolutePath());
		
		planCharge = new Plan(fichier.getName());
		System.out.println("Plan chargé : " + planCharge);
		
		this.latMax = planCharge.getIntersections().values().stream()
				.map(intersection -> intersection.getLatitude())
				.max((a,b) -> Float.compare(a, b)).orElse(0f);
		this.latMin = planCharge.getIntersections().values().stream()
				.map(intersection -> intersection.getLatitude())
				.min((a,b) -> Float.compare(a, b)).orElse(0f);
		this.longMax = planCharge.getIntersections().values().stream()
				.map(intersection -> intersection.getLongitude())
				.max((a,b) -> Float.compare(a, b)).orElse(0f);
		this.longMin = planCharge.getIntersections().values().stream()
				.map(intersection -> intersection.getLongitude())
				.min((a,b) -> Float.compare(a, b)).orElse(0f);
		
		this.largeurPlan = this.latMax - this.latMin;
		this.hauteurPlan = this.longMax - this.longMin;
		
		this.echelle = Math.min(this.canvasPlan.getWidth() / this.largeurPlan, this.canvasPlan.getHeight() / this.hauteurPlan);
		
		//System.out.println("hauteur="+this.hauteurPlan+ " largeur="+this.largeurPlan + "echelle="+this.echelle);
		
		GraphicsContext gc = canvasPlan.getGraphicsContext2D();
		
		for(Intersection intersection: planCharge.getIntersections().values()) {
			gc.strokeOval(
					convertirLatitudeEnX(intersection.getLatitude())-(this.TAILLE_CERCLE_INTERSECTION/2), 
					convertirLongitudeEnY(intersection.getLongitude())-(this.TAILLE_CERCLE_INTERSECTION/2), 
					this.TAILLE_CERCLE_INTERSECTION, this.TAILLE_CERCLE_INTERSECTION);
		}
		for(Segment segment: planCharge.getSegments()) {
			gc.strokeLine(
					convertirLatitudeEnX(segment.getOrigine().getLatitude()),
					convertirLongitudeEnY(segment.getOrigine().getLongitude()), 
					convertirLatitudeEnX(segment.getDestination().getLatitude()), 
					convertirLongitudeEnY(segment.getDestination().getLongitude()));
		}
		
		gc.setStroke(this.COULEUR_DEPOT);
		gc.setFill(this.COULEUR_DEPOT);
		gc.strokeOval(
				convertirLatitudeEnX(planCharge.getEntrepot().getLatitude())-(this.TAILLE_CERCLE_INTERSECTION/2), 
				convertirLongitudeEnY(planCharge.getEntrepot().getLongitude())-(this.TAILLE_CERCLE_INTERSECTION/2), 
				this.TAILLE_CERCLE_INTERSECTION, this.TAILLE_CERCLE_INTERSECTION);
	}

	private void actionClicSurCanvas(MouseEvent event) {
		if(this.planCharge != null) {
			System.out.println("Clic sur le canvas, (x,y)=("+event.getX()+","+event.getY()
				+") (lat,long)="+convertirXEnLatitude(event.getX())+","+convertirYEnLongitude(event.getY()));
			Intersection intersectionTrouvee = this.trouverIntersectionCoordoneesPixels(event.getX(), event.getY());
			if (intersectionTrouvee != null) {
				textfieldIdentifiantIntersection.setText(intersectionTrouvee.getIdIntersection().toString());
			} else {
				textfieldIdentifiantIntersection.setText("");
			}
		} else {
			System.out.println("Clic sur le canvas, (x,y)=("+event.getX()+","+event.getY()+")");
		}
		
	}
	
	/**
	 * Trouve l'intersection du plan qui se trouve aux coordonnées x,y 
	 * (en pixels) avec une marge d'erreur égale à la taille des cercles 
	 * d'intersection (TAILLE_CERCLE_INTERSECTION). Si deux intersections
	 * correspondent (à cause de la marge d'erreur), la plus proche est
	 * sélectionnée.
	 * @param x coordonnée en x (pixels)
	 * @param y coordonnée en y (pixels)
	 * @return l'intersection du plan si une intersection se trouve 
	 * aux coordonnées précisées, null sinon
	 */
	private Intersection trouverIntersectionCoordoneesPixels(double x, double y) {
		if (this.planCharge != null) {
			// trouver les intersections de la zone de clic 
			List<Intersection> candidats = new ArrayList<>();
			for (Intersection intersection: planCharge.getIntersections().values()) {
				if (distance(convertirLatitudeEnX(intersection.getLatitude()), 
						convertirLongitudeEnY(intersection.getLongitude()), x, y) 
					  <= this.TAILLE_CERCLE_INTERSECTION) {
					candidats.add(intersection);
				}
			}
			
			if (candidats.isEmpty()) {
				return null;
			} else if (candidats.size() == 1) {
				return candidats.get(0);
			} else {
				return candidats.stream()
						.min((i1, i2) -> Double.compare(
								distance(convertirLatitudeEnX(i1.getLatitude()), convertirLongitudeEnY(i1.getLongitude()), x, y),  
								distance(convertirLatitudeEnX(i2.getLatitude()), convertirLongitudeEnY(i2.getLongitude()), x, y)))
						.orElse(null);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Donne la distance qui sépare deux points.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1-x2,2)-Math.pow(y1-y2, 2));
	}

	private void actionBoutonAjouterLivraison(ActionEvent event) {
		System.out.println("Clic sur le bouton Ajouter Livraison, event = " + event);
	}
	
	private double convertirLatitudeEnX(double x) {
		return (x-this.latMin) * this.echelle;
	}
	private double convertirLongitudeEnY(double y) {
		return (y-this.longMin) * this.echelle;
	}
	private double convertirXEnLatitude(double x) {
		return x / this.echelle + this.latMin;
	}
	private double convertirYEnLongitude(double x) {
		return x / this.echelle + this.longMin;
	}
	
	public void setStage(Stage stage) {
		this.thisStage = stage;
	}
}
