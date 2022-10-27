/*
 * ControleurFenetrePrincipale
 * 
 * Version 1.0
 */

package main.controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.scene.paint.CycleMethod;
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
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import main.modele.DemandeLivraison;
import main.modele.Intersection;
import main.modele.Journee;
import main.modele.PlageHoraire;
import main.modele.Plan;
import main.modele.Segment;
import main.modele.Tournee;
import main.modele.Trajet;

/**
 * Contrôleur de la vue principale de l'application.
 * @author H4113
 *
 */
public class ControleurFenetrePrincipale {
    
    static final Logger logger = LogManager.getLogger(ControleurFenetrePrincipale.class);
    final int ARR_SIZE = 5;
    final double TAILLE_RECT_PT_LIVRAISON = 8;
    final double TAILLE_RECT_PT_LIVRAISON_SELECTIONNE = 12;
    final double TAILLE_CERCLE_INTERSECTION_SELECTIONNEE = 8;
	final double TAILLE_CERCLE_INTERSECTION = 5;
	final Color COULEUR_DEPOT = Color.RED;
	final Color COULEUR_INTERSECTION = Color.BLACK;
	final Color COULEUR_SEGMENT = Color.BLACK;
	final Color COULEUR_POINT_LIVRAISON = Color.BLUE;
	final Color COULEUR_POINT_LIVRAISON_SELECTIONNE = Color.RED;
	/* Rayon en pixels définissant la zone où l'on 
	 reconnaît les intersections ciblées*/
	final double RAYON_TOLERANCE_CLIC_INTERSECTION = 8; 
	
	// Etats
    Etat etatCourant;
    final EtatInitial etatInitial = new EtatInitial();
    final EtatSansDemande etatSansDemande = new EtatSansDemande();
    final EtatAvecDemande etatAvecDemande = new EtatAvecDemande();
    final EtatAfficherFeuillesRoute etatAfficherFeuillesRoute = new EtatAfficherFeuillesRoute();
    final EtatDemandeLivraisonSelectionneeAvecTournees etatDemandeLivraisonSelectionneeAvecTournees = new EtatDemandeLivraisonSelectionneeAvecTournees();
    final EtatDemandeLivraisonSelectionneeSansTournees etatDemandeLivraisonSelectionneeSansTournees = new EtatDemandeLivraisonSelectionneeSansTournees();
    final EtatModifierDemandeLivraisonAvecTournees etatModifierDemandeLivraisonAvecTournees = new EtatModifierDemandeLivraisonAvecTournees();
    final EtatModifierDemandeLivraisonSansTournees etatModifierDemandeLivraisonSansTournees = new EtatModifierDemandeLivraisonSansTournees();
    final EtatSaisieNouvelleDemandeAvecTournees etatSaisieNouvelleDemandeAvecTournees = new EtatSaisieNouvelleDemandeAvecTournees();
    final EtatSaisieNouvelleDemandeSansTournees etatSaisieNouvelleDemandeSansTournees = new EtatSaisieNouvelleDemandeSansTournees();
    final EtatTourneesCalculees etatTourneesCalculees = new EtatTourneesCalculees();
	
	@Setter
	Stage stage;
	float largeurPlan;
	float hauteurPlan;
	double echelle;
    
	Float latMax;
	Float latMin;
	Float longMax;
	Float longMin;

	// modèle
	Journee journee;
	Plan planCharge;

	// objets FXML
	@FXML
	Button buttonValiderLivraison;
	@FXML
	Button buttonAnnulerLivraison;
	@FXML
	Button buttonAutoriserAjouterLivraison;
	@FXML
	Button buttonCalculerTournees;
	@FXML
	Button buttonSauvegarderDemandes;
	@FXML
	Button buttonChargerDemandes;
	@FXML
	Button buttonAfficherFeuillesRoute;
	@FXML
	Button buttonChargerPlan;
	@FXML
	Button buttonSupprimerLivraison;
	@FXML
	Button buttonModifierLivraison;
	@FXML 
	Button buttonEtatCourant;
	@FXML
	TableView<DemandeLivraison> tableViewDemandesLivraison;
	@FXML
	Canvas canvasPlan;
	@FXML
	Canvas canvasInterieurPlan;
	@FXML
	Canvas canvasPlanTrajet;
	@FXML
	ComboBox<PlageHoraire> comboboxPlageHoraire;
	@FXML
	TextField textfieldIdentifiantIntersection;
	@FXML
	TextField textfieldNomFichier;
	@FXML
	TableColumn<DemandeLivraison, Long> columnIdentifiant;
	@FXML
	TableColumn<DemandeLivraison, PlageHoraire> columnPlageHoraire;
	@FXML
	TitledPane titlePaneSelectionDemande;
	//public ImageView im = new ImageView(".\\data\\repere.png");
	@FXML
	TextField textfieldIdentifiantIntersectionSelection;
	@FXML
	TextField textfieldPlageHoraire;

	@FXML
    private void handleKeyPressed(KeyEvent ke){
    	 this.etatCourant.touchePressee(this, ke);
    }
	
	@FXML
	private void initialize() {
	    this.etatCourant = this.etatInitial;
	    final LoggerContext context = (LoggerContext) LogManager.getContext(false);
	    final org.apache.logging.log4j.core.config.Configuration config = context.getConfiguration();
	    config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.ALL);
	    config.getLoggerConfig(ControleurFenetrePrincipale.class.getPackage().getName()).setLevel(Level.ALL);
	    context.updateLoggers(config);
	    
	    buttonSupprimerLivraison.setOnAction(event -> actionBoutonSupprimerLivraison(event));
	    buttonModifierLivraison.setOnAction(event -> actionBoutonModifierLivraison(event));
	    buttonSupprimerLivraison.setDisable(true);
	    buttonModifierLivraison.setDisable(true);
	    
	    buttonValiderLivraison.setDisable(true);
	    buttonAnnulerLivraison.setDisable(true);
	    comboboxPlageHoraire.setDisable(true);
	    
	    buttonEtatCourant.setOnAction(event -> System.out.println("Etat courant = " + this.etatCourant.getClass().getName()));
	    
	    buttonValiderLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
	    buttonAnnulerLivraison.setOnAction(event -> actionBoutonAnnulerLivraison(event));
	    buttonAutoriserAjouterLivraison.setOnAction(event -> actionBoutonAutoriserAjouterLivraison(event));
		buttonChargerDemandes.setOnAction(event -> actionBoutonChargerDemande(event));
		buttonSauvegarderDemandes.setOnAction(event -> actionBoutonSauvegarderDemandes(event));
		canvasInterieurPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
		tableViewDemandesLivraison.setOnMouseClicked(event -> actionClicTableau(event));
		buttonChargerPlan.setOnAction(event -> actionBoutonChargerPlan(event));
	    buttonCalculerTournees.setOnAction(event -> actionBoutonCalculerTournees(event));
	    
	
        tableViewDemandesLivraison.setItems(FXCollections.observableArrayList());
        columnIdentifiant.setCellValueFactory(
                new PropertyValueFactory<DemandeLivraison, Long>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(
                new PropertyValueFactory<DemandeLivraison, PlageHoraire>("plageHoraire")); 
	    
		journee = new Journee();
		for(int i=8; i<12; i++) {
		    comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
		}
	}
	
	private void actionBoutonSupprimerLivraison(ActionEvent event) {
	    etatCourant.supprimerDemande(this);
	}
	private void actionClicTableau(MouseEvent event) {
	    this.etatCourant.clicGaucheSurTableau(this);
	}
	
	private void actionBoutonAnnulerLivraison(ActionEvent event2) {
	    this.etatCourant.annulerAjouterOuModifier(this);
	}
	
    private void actionBoutonAutoriserAjouterLivraison(ActionEvent event2) {
        etatCourant.ajouterDemande(this);
    }
    
    private void actionBoutonModifierLivraison(ActionEvent event) {
        etatCourant.modifierDemande(this);
    }
    
    private void actionBoutonChargerPlan(ActionEvent event) {
		etatCourant.chargerPlan(this);
	}

    /**
     * Action à effectuer lors du clic sur le Canvas.
     * @param event MouseEvent associé
     */
	private void actionClicSurCanvas(MouseEvent event) {
		etatCourant.clicGaucheSurPlan(this, event);
	}


	/**
	 * Action à effectuer lors du clic sur le bouton Charger demandes.
	 * Ouvre un explorateur de fichier pour choisir le fichier à charger.
	 * @param event ActionEvent associé
	 */
	private void actionBoutonChargerDemande(ActionEvent event) {
	    etatCourant.chargerListeDemandes(this);
	}
	
	private void actionBoutonCalculerTournees(ActionEvent event) {
        this.etatCourant.calculerTournees(this);
    }
    
	void mettreAJourCanvasDemande() {
        GraphicsContext gc = canvasInterieurPlan.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasInterieurPlan.getWidth(), canvasInterieurPlan.getHeight());
                
        for(DemandeLivraison d: journee.getDemandesLivraison()) {
            this.dessinerIntersectionLatLong(gc,
                    d.getIntersection().getLatitude(), 
                    d.getIntersection().getLongitude(), 
                    this.COULEUR_POINT_LIVRAISON, 
                    this.TAILLE_RECT_PT_LIVRAISON, 
                    true, 
                    "Rectangle");
        }        
        columnIdentifiant.setCellValueFactory(
                new PropertyValueFactory<DemandeLivraison, Long>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(
                new PropertyValueFactory<DemandeLivraison, PlageHoraire>("plageHoraire")); 
        
    }
	
    private void actionBoutonAjouterLivraison(ActionEvent event) {
        etatCourant.validerAjouterOuModifier(this);
    }
	
	/**
	 * Action à effectuer lors du clic sur le bouton Sauvegarder demandes.
	 * Ouvre un explorateur de fichier pour choisir l'emplacement du fichier.
	 * @param event ActionEvent associé
	 */
    private void actionBoutonSauvegarderDemandes(ActionEvent event) {
        etatCourant.sauvegarderDemandes(this);
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
	Intersection trouverIntersectionCoordoneesPixels(double x, double y) {
		if (this.planCharge != null) {
			// trouver les intersections de la zone de clic
			List<Intersection> candidats = new ArrayList<>();
			for (Intersection intersection : planCharge.getIntersections().values()) {
				if (distance(convertirLongitudeEnX(intersection.getLongitude()),
						     convertirLatitudeEnY(intersection.getLatitude()), 
						     x, y) 
				        <= this.RAYON_TOLERANCE_CLIC_INTERSECTION) {
					candidats.add(intersection);
				}
			}
			/*
			logger.debug("candidats = " + candidats);
			candidats.forEach(
			        i -> System.out.println("lat,long = " 
			                + i.getLatitude()+","+i.getLongitude()+" x,y = "
			                + this.convertirLongitudeEnX(i.getLongitude())
			                +","+this.convertirLatitudeEnY(i.getLatitude())));
							*/
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
	double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/**
	 * Dessine une intersection sur le canvas à partir des latitudes 
	 * et longitudes fournies, dans la couleur précisée.
	 * @param latitude 
	 * @param longitude
	 * @param couleur
	 * @param remplir
	 * @param forme -> parmi "rectangle", "circle"
	 */
	void dessinerIntersectionLatLong(GraphicsContext gc,
	                                         double latitude, 
                                	         double longitude, 
                                	         Color couleur,
                                	         double taille,
                                	         boolean remplir,
                                	         String forme) {
        dessinerIntersectionXY(gc,
                                convertirLongitudeEnX(longitude),
                                convertirLatitudeEnY(latitude), 
                                couleur,
                                taille,
                                remplir,
                                forme);
	}
	
	/**
	 * Dessiner une intersection sur le canvas à partir des coordonnées
	 * X et Y fournies, dans la couleur précisée. Les coordonnées
	 * indiquent l'emplacement exact de l'intersection.
	 * @param x
	 * @param y
	 * @param couleur
	 * 
	 */
	void dessinerIntersectionXY(GraphicsContext gc,
	                                    double x, 
                            	        double y, 
                            	        Color couleur,
                            	        double taille,
                            	        boolean remplir,
                            	        String forme) {
	    if(remplir) {
	        gc.setFill(couleur);
	        if(forme == "Rectangle") {
	            gc.fillRect(x - (taille /2), 
	                          y - (taille /2),
	                          taille, 
	                          taille);
	        } else if(forme == "Cercle") {
	            gc.fillOval(x - (taille /2), 
                        y - (taille /2),
                        taille, 
                        taille);
	        }
	    } else {
	        gc.setStroke(couleur);
	        if(forme == "Rectangle") {
	            gc.strokeRect(x - (taille /2), 
                        y - (taille /2),
                        taille, 
                        taille);
	        } else if(forme == "Cercle") {
                gc.strokeOval(x - (taille /2), 
                        y - (taille /2),
                        taille, 
                        taille);
	        }
	    }
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
	void dessinerSegmentLatLong(double lat1, double long1, 
	                                    double lat2, double long2, 
	                                    Color couleur) {
	    dessinerSegmentXY(convertirLongitudeEnX(long1),
	            convertirLatitudeEnY(lat1),
	            convertirLongitudeEnX(long2),
	            convertirLatitudeEnY(lat2),
	            couleur);
	}
	
	
	void dessinerTrajetLatLong(double lat1, double long1, 
            double lat2, double long2) {
        dessinerSegmentGradientXY(convertirLongitudeEnX(long1),
                convertirLatitudeEnY(lat1),
                convertirLongitudeEnX(long2),
                convertirLatitudeEnY(lat2));
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
	void dessinerSegmentXY(double x1, double y1, 
                        	       double x2, double y2, 
                        	       Color couleur) {
	    GraphicsContext gc = canvasPlan.getGraphicsContext2D();
	    gc.setStroke(couleur);
	    gc.strokeLine(x1, y1, x2, y2);
	}
	
	private void dessinerSegmentGradientXY(double x1, double y1, 
            double x2, double y2) {
     GraphicsContext gc = canvasPlanTrajet.getGraphicsContext2D();
     
    //     Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.MAROON)};
    //     LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.REFLECT, stops);
     
         gc.setLineWidth(3);
         gc.setStroke(Color.DODGERBLUE);
         gc.strokeLine(x1, y1, x2, y2);
         

    
         //this.drawArrow(gc, (int)(x1), (int)(y1), (int)(x2), (int)(y2));
    }
	
	// depuis https://stackoverflow.com/questions/35751576/javafx-draw-line-with-arrow-canvas
	
	private void drawArrow(GraphicsContext gc, int x1, int y1, int x2, int y2) {
	    gc.setFill(Color.BLUE);

	    double dx = x2 - x1, dy = y2 - y1;
	    double angle = Math.atan2(dy, dx);
	    int len = (int) Math.sqrt(dx * dx + dy * dy);
	  
	    Transform transform = Transform.translate(x1, y1);
	    transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
	    gc.setTransform(new Affine(transform));
	    gc.setLineWidth(3);    
	    gc.strokeLine(0, 0, len, 0);
	    gc.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
	            4);
	}

	/**
	 * Convertit une longitude en pixels sur le Canvas (axe X). 
	 * @param x longitude 
	 * @return coordonnée X sur le Canvas
	 */
	double convertirLongitudeEnX(double x) {
		return /*this.canvasPlan.getHeight() -*/ (x - this.longMin) * this.echelle;
	}

	/**
     * Convertit une latitude en pixels sur le Canvas (axe Y). 
     * @param x latitude 
     * @return coordonnée Y sur le Canvas
     */
	double convertirLatitudeEnY(double y) {
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
	double convertirXEnLongitude(double x) {
		return this.longMin +/*- */(x /*- this.canvasPlan.getHeight()*/) / this.echelle;
	}

	/**
     * Convertit une coordonnées en pixels sur l'axe Y en latitude. 
     * @param y coordonnée Y sur le canvas 
     * @return latitude
     */
	double convertirYEnLatitude(double y) {
	    double aRemonter = this.canvasPlan.getWidth() - (this.latMax - this.latMin) * this.echelle;
		return this.latMin - (y + aRemonter - this.canvasPlan.getWidth()) / this.echelle;
	}
}
