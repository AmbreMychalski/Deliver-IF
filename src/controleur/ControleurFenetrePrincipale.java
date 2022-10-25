package controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/*
import modele.DemandeLivraison;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
*/
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Journee;
import modele.PlageHoraire;
import modele.Plan;
import modele.Segment;
import modele.TableRow;


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
	
	@FXML
	private Button buttonAjouterLivraison;
	@FXML
	private Button buttonAnnulerLivraison;
	@FXML
    private Button buttonAutoriserAjouterLivraison;
	@FXML
	private Button buttonCalculerTournees;
	@FXML
	private Button buttonSauvegarderDemandes;
	@FXML
	private Button buttonChargerDemandes;
	@FXML
	private Button buttonAfficherFeuillesRoute;
	@FXML
	private Button buttonChargerPlan;
	@FXML
	//private TableView tableViewDemandesLivraison;
	private TableView<TableRow> tableViewDemandesLivraison;
	@FXML
	private Canvas canvasPlan;
	@FXML
	private ComboBox<PlageHoraire> comboboxPlageHoraire;
	@FXML
	private TextField textfieldIdentifiantIntersection;
	@FXML
	private TextField textfieldNomFichier;
	@FXML
    private TableColumn<TableRow,Long> columnIdentifiant;
    @FXML
    public TableColumn<TableRow, PlageHoraire> columnPlageHoraire;
    
    //public ImageView im = new ImageView(".\\data\\repere.png");
    
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
	    buttonAjouterLivraison.setDisable(true);
	    buttonAnnulerLivraison.setDisable(true);
	    comboboxPlageHoraire.setDisable(true);
	    buttonAjouterLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
	    buttonAnnulerLivraison.setOnAction(event -> actionBoutonAnnulerLivraison(event));
	    buttonAutoriserAjouterLivraison.setOnAction(event -> actionBoutonAutoriserAjouterLivraison(event));
		buttonChargerDemandes.setOnAction(event -> actionBoutonChargerDemande(event));
		buttonSauvegarderDemandes.setOnAction(event -> actionBoutonSauvegarderDemandes(event));
		canvasPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
		buttonChargerPlan.setOnAction(event -> actionBoutonChargerPlan(event));
		journee = new Journee();
		for(int i=8; i<12; i++) {
		    comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
		}
	}
	
	private void actionBoutonAnnulerLivraison(ActionEvent event2) {
	    buttonAjouterLivraison.setDisable(true);
        buttonAnnulerLivraison.setDisable(true);
        comboboxPlageHoraire.setDisable(true);
	}
    private void actionBoutonAutoriserAjouterLivraison(ActionEvent event2) {
        if(planCharge != null) {
            buttonAjouterLivraison.setDisable(false);
            buttonAnnulerLivraison.setDisable(false);
            comboboxPlageHoraire.setDisable(false);
        }
    }
    
    private void actionBoutonChargerPlan(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		fileChooser.setTitle("Charger un plan");
		File fichier = fileChooser.showOpenDialog(this.thisStage);
		System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

		planCharge = new Plan(fichier);
		journee.setPlan(planCharge);
		
		System.out.println("Plan chargé : " + planCharge);

		this.latMax = planCharge.getIntersections().values().stream()
		        .map(intersection -> intersection.getLatitude())
				.max((a, b) -> Float.compare(a, b)).orElse(0f);
		this.latMin = planCharge.getIntersections().values().stream()
		        .map(intersection -> intersection.getLatitude())
				.min((a, b) -> Float.compare(a, b)).orElse(0f);
		this.longMax = planCharge.getIntersections().values().stream()
		        .map(intersection -> intersection.getLongitude())
				.max((a, b) -> Float.compare(a, b)).orElse(0f);
		this.longMin = planCharge.getIntersections().values().stream()
		        .map(intersection -> intersection.getLongitude())
				.min((a, b) -> Float.compare(a, b)).orElse(0f);

		this.largeurPlan = this.latMax - this.latMin;
		this.hauteurPlan = this.longMax - this.longMin;

		this.echelle = Math.min(this.canvasPlan.getWidth() / this.largeurPlan,
				this.canvasPlan.getHeight() / this.hauteurPlan);

		// System.out.println("hauteur="+this.hauteurPlan+ " largeur="+this.largeurPlan
		// + "echelle="+this.echelle);

		GraphicsContext gc = canvasPlan.getGraphicsContext2D();

		for (Intersection intersection : planCharge.getIntersections().values()) {
			gc.strokeOval(convertirLatitudeEnX(intersection.getLatitude()) - (this.TAILLE_CERCLE_INTERSECTION / 2),
					convertirLongitudeEnY(intersection.getLongitude()) - (this.TAILLE_CERCLE_INTERSECTION / 2),
					this.TAILLE_CERCLE_INTERSECTION, this.TAILLE_CERCLE_INTERSECTION);
		}
		for (Segment segment : planCharge.getSegments()) {
			gc.strokeLine(convertirLatitudeEnX(segment.getOrigine().getLatitude()),
					convertirLongitudeEnY(segment.getOrigine().getLongitude()),
					convertirLatitudeEnX(segment.getDestination().getLatitude()),
					convertirLongitudeEnY(segment.getDestination().getLongitude()));
		}

		gc.setStroke(this.COULEUR_DEPOT);
		gc.setFill(this.COULEUR_DEPOT);
		gc.strokeOval(
				convertirLatitudeEnX(planCharge.getEntrepot().getLatitude()) - (this.TAILLE_CERCLE_INTERSECTION / 2),
				convertirLongitudeEnY(planCharge.getEntrepot().getLongitude()) - (this.TAILLE_CERCLE_INTERSECTION / 2),
				this.TAILLE_CERCLE_INTERSECTION, this.TAILLE_CERCLE_INTERSECTION);
	}

	private void actionClicSurCanvas(MouseEvent event) {
		if (this.planCharge != null) {
			System.out.println("Clic sur le canvas, (x,y)=(" + event.getX() + "," + event.getY() + ") (lat,long)="
					+ convertirXEnLatitude(event.getX()) + "," + convertirYEnLongitude(event.getY()));
			Intersection intersectionTrouvee = this.trouverIntersectionCoordoneesPixels(event.getX(), event.getY());
			if (intersectionTrouvee != null) {
				textfieldIdentifiantIntersection.setText(intersectionTrouvee.getIdIntersection().toString());
			} else {
				textfieldIdentifiantIntersection.setText("");
			}
		} else {
			System.out.println("Clic sur le canvas, (x,y)=(" + event.getX() + "," + event.getY() + ")");
		}

	}
	 
	private void actionBoutonChargerDemande(ActionEvent event) {
	    FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger des demandes de livraison");
        File fichier = fileChooser.showOpenDialog(this.thisStage);
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        journee.chargerDemandesLivraison(fichier);
        this.mettreAJourListeDemandes();
        
	}
	
	private void mettreAJourListeDemandes() {
        ObservableList<TableRow> data = FXCollections.observableArrayList();
        for(DemandeLivraison d: journee.getDemandesLivraison()) {
            data.add(new TableRow(d));
        }
        tableViewDemandesLivraison.setItems(data);
        columnIdentifiant.setCellValueFactory(new PropertyValueFactory<TableRow, Long>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(new PropertyValueFactory<TableRow,PlageHoraire>("plageHoraire")); 
	}

	
    private void actionBoutonSauvegarderDemandes(ActionEvent event) {
        DirectoryChooser choixDossier = new DirectoryChooser();
        choixDossier.setInitialDirectory(new File(".\\data"));
        choixDossier.setTitle("Sauvegarder des demandes de livraison");
        File dossier = choixDossier.showDialog(this.thisStage);
        System.out.println("Dossier choisi = " + dossier.getAbsolutePath());
        
        if(!(dossier == null || textfieldNomFichier.getText() == "" || journee.getDemandesLivraison().isEmpty())) {
            File fichier = new File(dossier.getAbsolutePath()+"\\"+textfieldNomFichier.getText()+".xml");
            journee.sauvegarderDemandesLivraison(fichier);
        } else {
            System.err.print("Erreur lors de la sauvegarde des demandes");
        }
    }
    
    private void actionBoutonAjouterLivraison(ActionEvent event) {
        try {
            Intersection intersection = journee.getPlan().getIntersections().get(Long.parseLong(textfieldIdentifiantIntersection.getText()));
            PlageHoraire plageHoraire = comboboxPlageHoraire.getValue();
            if(intersection == null || plageHoraire == null) {
                throw (new Exception());
            }
            DemandeLivraison demande = new DemandeLivraison(intersection, plageHoraire);
            journee.ajouterDemandeLivraison(demande);
            this.mettreAJourListeDemandes();
            
        } catch (Exception ex) {
            System.err.println("Erreur lors de l'ajout de la demande");
        }
        
    }


	/**
	 * Trouve l'intersection du plan qui se trouve aux coordonnées x,y (en pixels)
	 * avec une marge d'erreur égale à la taille des cercles d'intersection
	 * (TAILLE_CERCLE_INTERSECTION). Si deux intersections correspondent (à cause de
	 * la marge d'erreur), la plus proche est sélectionnée.
	 * 
	 * @param x coordonnée en x (pixels)
	 * @param y coordonnée en y (pixels)
	 * @return l'intersection du plan si une intersection se trouve aux coordonnées
	 *         précisées, null sinon
	 */
	private Intersection trouverIntersectionCoordoneesPixels(double x, double y) {
		if (this.planCharge != null) {
			// trouver les intersections de la zone de clic
			List<Intersection> candidats = new ArrayList<>();
			for (Intersection intersection : planCharge.getIntersections().values()) {
				if (distance(convertirLatitudeEnX(intersection.getLatitude()),
						convertirLongitudeEnY(intersection.getLongitude()), x, y) <= this.TAILLE_CERCLE_INTERSECTION) {
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
								distance(convertirLatitudeEnX(i1.getLatitude()),
										convertirLongitudeEnY(i1.getLongitude()), x, y),
								distance(convertirLatitudeEnX(i2.getLatitude()),
										convertirLongitudeEnY(i2.getLongitude()), x, y)))
						.orElse(null);
			}
		} else {
			return null;
		}
	}

	/**
	 * Donne la distance qui sépare deux points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) - Math.pow(y1 - y2, 2));
	}

	private double convertirLatitudeEnX(double x) {
		return (x - this.latMin) * this.echelle;
	}

	private double convertirLongitudeEnY(double y) {
		return (y - this.longMin) * this.echelle;
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
