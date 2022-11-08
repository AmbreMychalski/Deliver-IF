package vue;

import controleur.ControleurFenetrePrincipale;
import exception.FichierNonConformeException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import modele.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.ArrayList;
import java.util.List;

public class VueFenetrePrincipale {

    ControleurFenetrePrincipale controleur;

    final int ARR_SIZE = 5;
    public final double TAILLE_RECT_PT_LIVRAISON = 8;
    public final double TAILLE_RECT_PT_LIVRAISON_SELECTIONNE = 12;
    public final double TAILLE_CERCLE_INTERSECTION_SELECTIONNEE = 8;
    public final double TAILLE_CERCLE_INTERSECTION = 5;
    public final Color COULEUR_DEPOT = Color.RED;
    public final Color COULEUR_SEGMENT = Color.BLACK;
    public final Color COULEUR_POINT_LIVRAISON = Color.BLUE;
    public final Color COULEUR_POINT_LIVRAISON_SELECTIONNE = Color.RED;
    /* Rayon en pixels définissant la zone où l'on
     reconnaît les intersections ciblées*/
    public final double RAYON_TOLERANCE_CLIC_INTERSECTION = 8;

    @Getter
    @Setter
    Stage stage;
    public float largeurPlan;
    public float hauteurPlan;
    public double echelle;

    public Float latMax;
    public Float latMin;
    public Float longMax;
    public Float longMin;

    // objets FXML
    @FXML
    public Button buttonValiderLivraison;
    @FXML
    public Button buttonAnnulerLivraison;
    @FXML
    public Button buttonAutoriserAjouterLivraison;
    @FXML
    public Button buttonCalculerTournees;
    @FXML
    public Button buttonSauvegarderDemandes;
    @FXML
    public Button buttonChargerDemandes;
    @FXML
    public Button buttonAfficherFeuillesRoute;
    @FXML
    public Button buttonChargerPlan;
    @FXML
    public Button buttonSupprimerLivraison;
    @FXML
    public Button buttonModifierLivraison;
    @FXML
    public Button buttonAjouterLivreur;
    @FXML
    public Button buttonEtatCourant;
    @FXML
    public TableView<DemandeLivraison> tableViewDemandesLivraison;
    @FXML
    public Canvas canvasPlan;
    @FXML
    public Canvas canvasInterieurPlan;
    @FXML
    public Canvas canvasPlanTrajet;
    @FXML
    public ComboBox<PlageHoraire> comboboxPlageHoraire;
    @FXML
    public TextField textfieldIdentifiantIntersection;
    @FXML
    public TableColumn<DemandeLivraison, Long> columnIdentifiant;
    @FXML
    public TableColumn<DemandeLivraison, PlageHoraire> columnPlageHoraire;
    @FXML
    public TitledPane titlePaneSelectionDemande;
    //public ImageView im = new ImageView(".\\data\\repere.png");
    @FXML
    public TitledPane titledPaneEditionDemande;
    @FXML
    public TextField textfieldIdentifiantIntersectionSelection;
    @FXML
    public TextField textfieldPlageHoraire;
    @FXML
    public Label labelGuideUtilisateur;

    @FXML
    private void initialize() {
        controleur = new ControleurFenetrePrincipale(this);

        final LoggerContext context = (LoggerContext) LogManager.getContext(false);
        final org.apache.logging.log4j.core.config.Configuration config = context.getConfiguration();
        config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.ALL);
        config.getLoggerConfig(ControleurFenetrePrincipale.class.getPackage().getName()).setLevel(Level.ALL);
        context.updateLoggers(config);

        buttonSupprimerLivraison.setOnAction(this::actionBoutonSupprimerLivraison);
        buttonModifierLivraison.setOnAction(this::actionBoutonModifierLivraison);
        buttonAjouterLivreur.setOnAction(this::actionBoutonAjouterLivreur);
        buttonSupprimerLivraison.setDisable(true);
        buttonModifierLivraison.setDisable(true);
        buttonAjouterLivreur.setDisable(true);

        buttonValiderLivraison.setDisable(true);
        buttonAnnulerLivraison.setDisable(true);
        comboboxPlageHoraire.setDisable(true);

        buttonCalculerTournees.setDisable(true);
        buttonAutoriserAjouterLivraison.setDisable(true);
        buttonSauvegarderDemandes.setDisable(true);
        buttonChargerDemandes.setDisable(true);
        buttonAfficherFeuillesRoute.setDisable(true);

        textfieldIdentifiantIntersection.setDisable(true);
        textfieldPlageHoraire.setDisable(true);
        textfieldIdentifiantIntersectionSelection.setDisable(true);
        titlePaneSelectionDemande.setVisible(false);
        titledPaneEditionDemande.setVisible(false);

        buttonEtatCourant.setOnAction(event -> System.out.println("Etat courant = " + controleur.getEtatCourant().getClass().getName()));

        buttonValiderLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
        buttonAnnulerLivraison.setOnAction(event -> actionBoutonAnnulerLivraison(event));
        buttonAutoriserAjouterLivraison.setOnAction(event -> actionBoutonAutoriserAjouterLivraison(event));
        buttonChargerDemandes.setOnAction(event -> actionBoutonChargerDemande(event));
        buttonSauvegarderDemandes.setOnAction(event -> actionBoutonSauvegarderDemandes(event));
        canvasInterieurPlan.setOnMouseClicked(event -> actionClicSurCanvas(event));
        tableViewDemandesLivraison.setOnMouseClicked(event -> actionClicTableau(event));
        buttonChargerPlan.setOnAction(event -> {
            try {
                actionBoutonChargerPlan(event);
            } catch (FichierNonConformeException e) {
                ControleurFenetrePrincipale.logger.warn("Problème lors de la lecture du fichier");
            }
        });
        buttonCalculerTournees.setOnAction(event -> actionBoutonCalculerTournees(event));


        tableViewDemandesLivraison.setItems(FXCollections.observableArrayList());
        columnIdentifiant.setCellValueFactory(
                new PropertyValueFactory<>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(
                new PropertyValueFactory<>("plageHoraire"));
        columnPlageHoraire.setComparator(new PlageHoraireComparator());

        for(int i=8; i<12; i++) {
            comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
        }
    }


    @FXML
    private void handleKeyPressed(KeyEvent ke){
        controleur.touchePressee(ke);
    }

    private void actionBoutonSupprimerLivraison(ActionEvent event) {
        controleur.supprimerDemande();
    }
    private void actionClicTableau(MouseEvent event) {
        controleur.clicGaucheSurTableau(event);
    }

    private void actionBoutonAnnulerLivraison(ActionEvent event2) {
        controleur.annulerAjouterOuModifier();
    }

    private void actionBoutonAutoriserAjouterLivraison(ActionEvent event2) {
        controleur.ajouterDemande();
    }

    private void actionBoutonModifierLivraison(ActionEvent event) {
        controleur.modifierDemande();
    }

    private void actionBoutonAjouterLivreur(ActionEvent event) {
        controleur.ajouterLivreur();
    }

    private void actionBoutonChargerPlan(ActionEvent event) throws FichierNonConformeException {
        controleur.chargerPlan();
    }

    /**
     * Action à effectuer lors du clic sur le Canvas.
     * @param event MouseEvent associé
     */
    private void actionClicSurCanvas(MouseEvent event) {
        controleur.clicGaucheSurPlan(event);
    }

    /**
     * Action à effectuer lors du clic sur le bouton Charger demandes.
     * Ouvre un explorateur de fichier pour choisir le fichier à charger.
     * @param event ActionEvent associé
     */
    private void actionBoutonChargerDemande(ActionEvent event) {
        controleur.chargerListeDemandes();
    }

    private void actionBoutonCalculerTournees(ActionEvent event) {
        controleur.calculerTournees();
    }

    public void afficherDemandeLivraison(boolean nettoyerCanvas) {
        GraphicsContext gc = canvasInterieurPlan.getGraphicsContext2D();
        if(nettoyerCanvas){
            gc.clearRect(0, 0, canvasInterieurPlan.getWidth(), canvasInterieurPlan.getHeight());
        }

        for(DemandeLivraison d: controleur.getJournee().getDemandesLivraison()) {
            this.dessinerIntersection(gc,
                    d.getIntersection(),
                    d.getPlageHoraire().getCouleur(),
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
        controleur.validerAjouterOuModifier();
    }

    /**
     * Action à effectuer lors du clic sur le bouton Sauvegarder demandes.
     * Ouvre un explorateur de fichier pour choisir l'emplacement du fichier.
     * @param event ActionEvent associé
     */
    private void actionBoutonSauvegarderDemandes(ActionEvent event) {
        controleur.sauvegarderDemandes();
    }

    public void updateLabelGuideUtilisateur(String texte){
        labelGuideUtilisateur.setText(texte);
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
    public Intersection trouverIntersectionCoordoneesPixels(double x, double y) {
        if (controleur.getJournee().getPlan() != null) {
            // trouver les intersections de la zone de clic
            List<Intersection> candidats = new ArrayList<>();
            for (Intersection intersection : controleur.getJournee().getPlan().getIntersections().values()) {
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
     * @param intersection
     * @param couleur
     * @param remplir
     * @param forme -> parmi "rectangle", "circle"
     */
    public void dessinerIntersection(GraphicsContext gc,
                                            Intersection intersection,
                                            Color couleur,
                                            double taille,
                                            boolean remplir,
                                            String forme) {
        dessinerIntersectionXY(gc,
                convertirLongitudeEnX(intersection.getLongitude()),
                convertirLatitudeEnY(intersection.getLatitude()),
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
            if(forme.equals("Rectangle")) {
                gc.fillRect(x - (taille /2),
                        y - (taille /2),
                        taille,
                        taille);
            } else if(forme.equals("Cercle")) {
                gc.fillOval(x - (taille /2),
                        y - (taille /2),
                        taille,
                        taille);
            }
        } else {
            gc.setStroke(couleur);
            if(forme.equals("Rectangle")) {
                gc.strokeRect(x - (taille /2),
                        y - (taille /2),
                        taille,
                        taille);
            } else if(forme.equals("Cercle")) {
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
     * @param segment Segment à dessiner
     * @param couleur
     */
    public void dessinerSegment(Segment segment, Color couleur) {
        dessinerSegmentXY(convertirLongitudeEnX(segment.getOrigine().getLongitude()),
                convertirLatitudeEnY(segment.getOrigine().getLatitude()),
                convertirLongitudeEnX(segment.getDestination().getLongitude()),
                convertirLatitudeEnY(segment.getDestination().getLatitude()),
                couleur);
    }


    public void dessinerTrajetLatLong(GraphicsContext gc, double lat1, double long1,
                                      double lat2, double long2) {
        dessinerSegmentGradientXY(gc, convertirLongitudeEnX(long1),
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

    private void dessinerSegmentGradientXY(GraphicsContext gc, double x1, double y1,
                                           double x2, double y2) {

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
     * @param y latitude
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
    public double convertirXEnLongitude(double x) {
        return this.longMin +/*- */(x /*- this.canvasPlan.getHeight()*/) / this.echelle;
    }

    /**
     * Convertit une coordonnées en pixels sur l'axe Y en latitude.
     * @param y coordonnée Y sur le canvas
     * @return latitude
     */
    public double convertirYEnLatitude(double y) {
        double aRemonter = this.canvasPlan.getWidth() - (this.latMax - this.latMin) * this.echelle;
        return this.latMin - (y + aRemonter - this.canvasPlan.getWidth()) / this.echelle;
    }

}
