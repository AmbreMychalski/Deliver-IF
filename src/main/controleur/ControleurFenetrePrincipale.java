/*
 * ControleurFenetrePrincipale
 * 
 * Version 1.0
 */

package main.controleur;

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
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.modele.DemandeLivraison;
import main.modele.Intersection;
import main.modele.Journee;
import main.modele.PlageHoraire;
import main.modele.Plan;
import main.modele.Segment;
import main.modele.LigneTableau;

/**
 * Contrôleur de la vue principale de l'application.
 * @author H4113
 *
 */
public class ControleurFenetrePrincipale {
    
    final double TAILLE_RECT_PT_LIVRAISON = 8;
    final double TAILLE_RECT_PT_LIVRAISON_SELECTIONNE = 12;
    final double TAILLE_CERCLE_INTERSECTION_SELECTIONNEE = 8;
	final double TAILLE_CERCLE_INTERSECTION = 5;
	final Color COULEUR_DEPOT = Color.RED;
	final Color COULEUR_INTERSECTION = Color.BLACK;
	final Color COULEUR_SEGMENT = Color.BLACK;
	final Color COULEUR_POINT_LIVRAISON = Color.BLUE;
	final Color COULEUR_POINT_LIVRAISON_SELECTIONNE = Color.RED;
	Stage thisStage;
	float largeurPlan;
	float hauteurPlan;
	double echelle;

	// modèle
	Journee journee;
	Plan planCharge;

	// objets FXML
	@FXML
	private Button buttonValiderLivraison;
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
	//private TableView tableViewDemandesLivraison;
	@FXML
	private TableView<LigneTableau> tableViewDemandesLivraison;
	@FXML
	private Canvas canvasPlan;
	@FXML
	private Canvas canvasInterieurPlan;
	@FXML
	private ComboBox<PlageHoraire> comboboxPlageHoraire;
	@FXML
	private TextField textfieldIdentifiantIntersection;
	@FXML
	private TextField textfieldNomFichier;
	@FXML
    private TableColumn<LigneTableau,Long> columnIdentifiant;
    @FXML
    public TableColumn<LigneTableau, PlageHoraire> columnPlageHoraire;
    @FXML
    public Button buttonSupprimerLivraison;
    @FXML
    public TitledPane titlePaneSelectionDemande;
    //public ImageView im = new ImageView(".\\data\\repere.png");
    @FXML
    public TextField textfieldIdentifiantIntersectionSelection;
    @FXML
    public TextField textfieldPlageHoraire;
    
	private Float latMax;
	private Float latMin;
	private Float longMax;
	private Float longMin;

	@FXML
	private void initialize() {
	    titlePaneSelectionDemande.setVisible(false);
	    buttonSupprimerLivraison.setOnAction(event -> actionBoutonSupprimerLivraison(event));
	    
	    buttonValiderLivraison.setDisable(true);
	    buttonAnnulerLivraison.setDisable(true);
	    comboboxPlageHoraire.setDisable(true);
	    
	    buttonValiderLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
	    buttonAnnulerLivraison.setOnAction(event -> actionBoutonAnnulerLivraison(event));
	    buttonAutoriserAjouterLivraison.setOnAction(event -> actionBoutonAutoriserAjouterLivraison(event));
		buttonChargerDemandes.setOnAction(event -> actionBoutonChargerDemande(event));
		buttonSauvegarderDemandes.setOnAction(event -> actionBoutonSauvegarderDemandes(event));
		canvasInterieurPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
		tableViewDemandesLivraison.setOnMouseClicked(event -> actionClicTableau(event));
		buttonChargerPlan.setOnAction(event -> actionBoutonChargerPlan(event));
		journee = new Journee();
		for(int i=8; i<12; i++) {
		    comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
		}
	}
	
	private void actionBoutonSupprimerLivraison(ActionEvent event) {
	    LigneTableau ligne = tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
	    if(ligne != null) {
	        journee.supprimerDemandeLivraison(ligne.getDemandeLivraison());
	        this.mettreAJourListeDemandes();
	        textfieldIdentifiantIntersectionSelection.setText("");
	        textfieldPlageHoraire.setText("");
	    }
	}
	private void actionClicTableau(MouseEvent event) {
	    LigneTableau ligne = tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
	    if (ligne != null) {
	        mettreAJourListeDemandes();
	        dessinerPointLivraison(convertirLongitudeEnX(ligne.getDemandeLivraison().getIntersection().getLongitude()),
                    convertirLatitudeEnY(ligne.getDemandeLivraison().getIntersection().getLatitude()),
                    this.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    COULEUR_POINT_LIVRAISON_SELECTIONNE);
	        titlePaneSelectionDemande.setVisible(true);
	        textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
	        textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
	    }
	}
	
	private void actionBoutonAnnulerLivraison(ActionEvent event2) {
	    buttonValiderLivraison.setDisable(true);
        buttonAnnulerLivraison.setDisable(true);
        comboboxPlageHoraire.setDisable(true);
	}
	
    private void actionBoutonAutoriserAjouterLivraison(ActionEvent event2) {
        if(planCharge != null) {
            buttonValiderLivraison.setDisable(false);
            buttonAnnulerLivraison.setDisable(false);
            comboboxPlageHoraire.setDisable(false);
        }
    }
    
    private void actionBoutonChargerPlan(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.getExtensionFilters().add(
		        new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
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

		this.largeurPlan = this.longMax - this.longMin;
		this.hauteurPlan = this.latMax - this.latMin;

		this.echelle = Math.min(this.canvasPlan.getWidth() / this.largeurPlan,
				this.canvasPlan.getHeight() / this.hauteurPlan);

		/*System.out.println("hauteur=" + this.hauteurPlan+ " largeur="
		                    + this.largeurPlan + "echelle=" + this.echelle);*/
		/*
		for (Intersection intersection : planCharge.getIntersections().values()) {
		    this.dessinerIntersectionLatLong(intersection.getLatitude(), 
		            intersection.getLongitude(), this.COULEUR_INTERSECTION);
		}
		*/
		for (Segment segment : planCharge.getSegments()) {
		    this.dessinerSegmentLatLong(segment.getOrigine().getLatitude(), 
		            segment.getOrigine().getLongitude(), 
		            segment.getDestination().getLatitude(), 
		            segment.getDestination().getLongitude(),
		            this.COULEUR_SEGMENT);
		}

		this.dessinerIntersectionLatLong(planCharge.getEntrepot().getLatitude(), 
		        planCharge.getEntrepot().getLongitude(), this.COULEUR_DEPOT);
		
	}

    /**
     * Action à effectuer lors du clic sur le Canvas.
     * @param event MouseEvent associé
     */
	private void actionClicSurCanvas(MouseEvent event) {
		if (this.planCharge != null) {
			System.out.println("Clic sur le canvas, (x,y)=(" 
			        + event.getX() + "," + event.getY() + ") (lat,long)="
					+ convertirYEnLatitude(event.getY()) + "," 
			        + convertirXEnLongitude(event.getX()));
			Intersection intersectionTrouvee = 
			        this.trouverIntersectionCoordoneesPixels(event.getX(), 
			                                                 event.getY());
			System.out.println("Intersection trouvée = " + intersectionTrouvee);
			if (intersectionTrouvee != null) {
				textfieldIdentifiantIntersection.setText(
				        intersectionTrouvee.getIdIntersection().toString());
				
				
				GraphicsContext gc = canvasInterieurPlan.getGraphicsContext2D();
				gc.clearRect(0, 0, canvasInterieurPlan.getWidth(), canvasInterieurPlan.getHeight());
		        gc.setFill(Color.DARKSLATEGRAY);
		        gc.fillOval(
		                this.convertirLongitudeEnX(intersectionTrouvee.getLongitude()) - (this.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE / 2),
		                this.convertirLatitudeEnY(intersectionTrouvee.getLatitude()) - (this.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE / 2),
		                this.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE, 
		                this.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE);
		        for(DemandeLivraison d: journee.getDemandesLivraison()) {
		            dessinerPointLivraison(convertirLongitudeEnX(d.getIntersection().getLongitude()),
		                    convertirLatitudeEnY(d.getIntersection().getLatitude()),
		                    this.TAILLE_RECT_PT_LIVRAISON,
		                    COULEUR_POINT_LIVRAISON);
		        }
				
			} else {
				textfieldIdentifiantIntersection.setText("");
			}
		} else {
			System.out.println("Clic sur le canvas, (x,y)=(" 
			                    + event.getX() + "," + event.getY() + ")");
		}

	}


	/**
	 * Action à effectuer lors du clic sur le bouton Charger demandes.
	 * Ouvre un explorateur de fichier pour choisir le fichier à charger.
	 * @param event ActionEvent associé
	 */
	private void actionBoutonChargerDemande(ActionEvent event) {
	    FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger des demandes de livraison");
        File fichier = fileChooser.showOpenDialog(this.thisStage);
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        journee.chargerDemandesLivraison(fichier);
        this.mettreAJourListeDemandes();
        
	}

	private void mettreAJourListeDemandes() {
        ObservableList<LigneTableau> data = FXCollections.observableArrayList();
        GraphicsContext gc = canvasInterieurPlan.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasInterieurPlan.getWidth(), canvasInterieurPlan.getHeight());
        
        for(DemandeLivraison d: journee.getDemandesLivraison()) {
            data.add(new LigneTableau(d));
            dessinerPointLivraison(convertirLongitudeEnX(d.getIntersection().getLongitude()),
                    convertirLatitudeEnY(d.getIntersection().getLatitude()),
                    this.TAILLE_RECT_PT_LIVRAISON,
                    COULEUR_POINT_LIVRAISON);
        }
        
        tableViewDemandesLivraison.setItems(data);
        columnIdentifiant.setCellValueFactory(
                new PropertyValueFactory<LigneTableau, Long>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(
                new PropertyValueFactory<LigneTableau,PlageHoraire>("plageHoraire")); 
	}
    
    private void actionBoutonAjouterLivraison(ActionEvent event) {
        try {
            Intersection intersection = journee.getPlan().getIntersections()
                    .get(Long.parseLong(textfieldIdentifiantIntersection
                            .getText()));
            PlageHoraire plageHoraire = comboboxPlageHoraire.getValue();
            if(intersection == null || plageHoraire == null) {
                throw (new Exception());
            }
            DemandeLivraison demande = 
                    new DemandeLivraison(intersection, plageHoraire);
            journee.ajouterDemandeLivraison(demande);
            this.mettreAJourListeDemandes();
            
        } catch (Exception ex) {
            System.err.println("Erreur lors de l'ajout de la demande");
        } finally {
            buttonValiderLivraison.setDisable(true);
            buttonAnnulerLivraison.setDisable(true);
            comboboxPlageHoraire.setDisable(true);
        }
        
    }
	
	/**
	 * Action à effectuer lors du clic sur le bouton Sauvegarder demandes.
	 * Ouvre un explorateur de fichier pour choisir l'emplacement du fichier.
	 * @param event ActionEvent associé
	 */
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
				if (distance(convertirLongitudeEnX(intersection.getLongitude()),
						     convertirLatitudeEnY(intersection.getLatitude()), 
						     x, y) 
				        <= this.TAILLE_CERCLE_INTERSECTION) {
					candidats.add(intersection);
				}
			}

			// sélectionner l'intersection à retourner
			if (candidats.isEmpty()) {
				return null;
			} else if (candidats.size() == 1) {
				return candidats.get(0);
			} else {
				return candidats.stream()
						.min((i1, i2) -> Double.compare(
								distance(convertirLongitudeEnX(i1.getLongitude()),
										convertirLatitudeEnY(i1.getLatitude()), 
										x, y),
								distance(convertirLongitudeEnX(i2.getLongitude()),
										convertirLatitudeEnY(i2.getLatitude()), 
										x, y)))
						.orElse(null);
			}
		} else {
			return null;
		}
	}

	/**
	 * Donne la distance qui sépare deux points.
	 * 
	 * @param x1 coordonnées en x du premier point
	 * @param y1 coordonnées en y du premier point
	 * @param x2 coordonnées en x du second point
	 * @param y2 coordonnées en y du second point
	 * @return distance entre les deux points (dans l'unité donnée en entrée)
	 */
	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) - Math.pow(y1 - y2, 2));
	}
	
	/**
	 * Dessine une intersection sur le canvas à partir des latitudes 
	 * et longitudes fournies, dans la couleur précisée.
	 * @param latitude 
	 * @param longitude
	 * @param couleur
	 */
	private void dessinerIntersectionLatLong(double latitude, 
                                	         double longitude, 
                                	         Color couleur) {
        dessinerIntersectionXY(convertirLongitudeEnX(longitude),
                convertirLatitudeEnY(latitude), couleur);
	}
	
	/**
	 * Dessiner une intersection sur le canvas à partir des coordonnées
	 * X et Y fournies, dans la couleur précisée. Les coordonnées
	 * indiquent l'emplacement exact de l'intersection.
	 * @param x
	 * @param y
	 * @param couleur
	 */
	private void dessinerIntersectionXY(double x, 
                            	        double y, 
                            	        Color couleur) {
	    GraphicsContext gc = canvasPlan.getGraphicsContext2D();
	    gc.setStroke(couleur);
	    gc.strokeOval(
                x - (this.TAILLE_CERCLE_INTERSECTION / 2),
                y - (this.TAILLE_CERCLE_INTERSECTION / 2),
                this.TAILLE_CERCLE_INTERSECTION, 
                this.TAILLE_CERCLE_INTERSECTION);
	}
	
	/**
	 * Dessine un segment entre deux points sur le canvas dans la
	 * couleur précisée. Les coordonnées sont données en latitude/longitude.
	 * @param lat1 Latitude du premier point
	 * @param long1 Longitude du premier point
	 * @param lat2 Latitude du second point
	 * @param long2 Longitude du second point
	 * @param couleur
	 */
	private void dessinerSegmentLatLong(double lat1, double long1, 
	                                    double lat2, double long2, 
	                                    Color couleur) {
	    dessinerSegmentXY(convertirLongitudeEnX(long1),
	            convertirLatitudeEnY(lat1),
	            convertirLongitudeEnX(long2),
	            convertirLatitudeEnY(lat2),
	            couleur);
	}
	
	private void dessinerPointLivraison(double x, double y, double taille, Color couleur) {
        GraphicsContext gc = canvasInterieurPlan.getGraphicsContext2D();
        gc.setFill(couleur);
        gc.fillRect(
                x - (taille /2), 
                y - (taille /2),
                taille, taille);
    }
	
	/**
	 * Dessine un segment entre deux points sur le canvas dans la
	 * couleur précisée. Les coodonnées sont données en pixels.
	 * @param x1 Coordonnée sur l'axe x du premier point
	 * @param y1 Coordonnée sur l'axe y du premier point
	 * @param x2 Coordonnée sur l'axe x du second point
	 * @param y2 Coordonnée sur l'axe y du second point
	 * @param couleur
	 */
	private void dessinerSegmentXY(double x1, double y1, 
                        	       double x2, double y2, 
                        	       Color couleur) {
	    GraphicsContext gc = canvasPlan.getGraphicsContext2D();
	    gc.setStroke(couleur);
	    gc.strokeLine(x1, y1, x2, y2);
	}

	/**
	 * Convertit une longitude en pixels sur le Canvas (axe X). 
	 * @param x longitude 
	 * @return coordonnée X sur le Canvas
	 */
	private double convertirLongitudeEnX(double x) {
		return /*this.canvasPlan.getHeight() -*/ (x - this.longMin) * this.echelle;
	}

	/**
     * Convertit une latitude en pixels sur le Canvas (axe Y). 
     * @param x latitude 
     * @return coordonnée Y sur le Canvas
     */
	private double convertirLatitudeEnY(double y) {
	    // quand on retourne la carte, elle n'est pas calée en haut, 
	    // on la cale donc en lui retirant "aRemonter"
	    double aRemonter = this.canvasPlan.getWidth() - (this.latMax - this.latMin) * this.echelle;
		return (this.canvasPlan.getWidth() - (y - this.latMin) * this.echelle) - aRemonter;
	}

	/**
     * Convertit une coordonnées en pixels sur l'axe X en longitude. 
     * @param x coordonnée X sur le canvas 
     * @return longitude
     */
	private double convertirXEnLongitude(double x) {
		return this.longMin +/*- */(x /*- this.canvasPlan.getHeight()*/) / this.echelle;
	}

	/**
     * Convertit une coordonnées en pixels sur l'axe Y en latitude. 
     * @param y coordonnée Y sur le canvas 
     * @return latitude
     */
	private double convertirYEnLatitude(double y) {
	    double aRemonter = this.canvasPlan.getWidth() - (this.latMax - this.latMin) * this.echelle;
		return this.latMin - (y + aRemonter - this.canvasPlan.getWidth()) / this.echelle;
	}

	public void setStage(Stage stage) {
		this.thisStage = stage;
	}
}
