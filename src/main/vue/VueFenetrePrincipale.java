package vue;


import controleur.ControleurFenetrePrincipale;
import exception.FichierNonConformeException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import modele.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.*;

public class VueFenetrePrincipale implements Observer {

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

    public enum FormeIntersection { RECTANGLE, CERCLE };

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
    public Button buttonAssignerNvLivreur;
    @FXML
    public Button buttonEtatCourant;
    @FXML
    public TableView<DemandeLivraison> tableViewDemandesLivraison;
    @FXML
    public TableView<Livraison> tableViewLivraisons;
    @FXML
    public Canvas canvasPlan;
    @FXML
    public Canvas canvasIntersectionsLivraisons;
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
    public TableColumn<Livraison, Long> columnIdentifiantLivraison;
    @FXML
    public TableColumn<Livraison, PlageHoraire> columnPlageHoraireLivraison;
    @FXML
    public TableColumn<Livraison, String> columnHeure;
    @FXML
    public TableColumn<Livraison, Integer> columnLivreur;
    @FXML
    public TitledPane titlePaneSelectionDemande;
    @FXML
    public TitledPane titledPaneEditionDemande;
    @FXML
    public TextField textfieldIdentifiantIntersectionSelection;
    @FXML
    public TextField textfieldPlageHoraire;
    @FXML
    public Label labelGuideUtilisateur;
    @FXML
    public ComboBox<Integer> comboboxLivreur;
    @FXML
    public Label labelRuesIntersection;

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
        buttonAssignerNvLivreur.setOnAction(this::actionBoutonAssignerNvLivreur);
        buttonSupprimerLivraison.setDisable(true);
        buttonModifierLivraison.setDisable(true);
        buttonAssignerNvLivreur.setDisable(true);

        buttonValiderLivraison.setDisable(true);
        buttonAnnulerLivraison.setDisable(true);
        comboboxPlageHoraire.setDisable(true);

        buttonCalculerTournees.setDisable(true);
        buttonAutoriserAjouterLivraison.setDisable(true);
        buttonSauvegarderDemandes.setDisable(true);
        buttonChargerDemandes.setDisable(true);
        buttonAfficherFeuillesRoute.setDisable(false);

        textfieldIdentifiantIntersection.setDisable(true);
        textfieldPlageHoraire.setDisable(true);
        textfieldIdentifiantIntersectionSelection.setDisable(true);
        titlePaneSelectionDemande.setVisible(false);
        titledPaneEditionDemande.setVisible(false);
        tableViewLivraisons.setVisible(false);
        buttonAfficherFeuillesRoute.setDisable(true);
        buttonEtatCourant.setOnAction(event -> System.out.println("Etat courant = " + controleur.getEtatCourant().getClass().getName()));
        buttonAfficherFeuillesRoute.setOnAction(event -> actionBoutonAfficherFeulleDeRoute(event));
        buttonValiderLivraison.setOnAction(event -> actionBoutonAjouterLivraison(event));
        buttonAnnulerLivraison.setOnAction(event -> actionBoutonAnnulerLivraison(event));
        buttonAutoriserAjouterLivraison.setOnAction(event -> actionBoutonAutoriserAjouterLivraison(event));
        buttonChargerDemandes.setOnAction(event -> {
            try{
                actionBoutonChargerDemande(event);
            }catch (Exception ex){
                System.err.println(ex);
            }
        });
        buttonSauvegarderDemandes.setOnAction(event -> actionBoutonSauvegarderDemandes(event));
        canvasIntersectionsLivraisons.setOnMouseClicked(event -> actionClicSurCanvas(event));
        tableViewDemandesLivraison.setOnMouseClicked(event -> actionClicTableau(event));
        tableViewLivraisons.setOnMouseClicked(event -> actionClicTableau(event));
        buttonChargerPlan.setOnAction(event -> {
            try {
                actionBoutonChargerPlan(event);
            } catch (Exception e) {
                System.err.println(e);
            }
        });
        buttonCalculerTournees.setOnAction(event -> actionBoutonCalculerTournees(event));


        tableViewDemandesLivraison.setItems(FXCollections.observableArrayList());
        columnIdentifiant.setCellValueFactory(
                new PropertyValueFactory<>("idIntersection"));
        columnPlageHoraire.setCellValueFactory(
                new PropertyValueFactory<>("plageHoraire"));
        columnPlageHoraire.setComparator(new ComparateurPlageHoraire());

        columnPlageHoraire.setCellFactory(
                new Callback<TableColumn<DemandeLivraison, PlageHoraire>, TableCell<DemandeLivraison, PlageHoraire>>() {
                    @Override
                    public TableCell<DemandeLivraison, PlageHoraire> call(TableColumn<DemandeLivraison, PlageHoraire> param) {
                        final TableCell<DemandeLivraison, PlageHoraire> tableCell = new TableCell<DemandeLivraison, PlageHoraire>() {
                            @Override public void updateItem(PlageHoraire plageHoraire, boolean empty) {
                                super.updateItem(plageHoraire, empty);
                                if (plageHoraire != null) {
                                    setText(plageHoraire.toString());
                                    setTextFill(plageHoraire.getCouleur());
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return tableCell;
                    }
                });

        tableViewLivraisons.setItems(FXCollections.observableArrayList());
        columnIdentifiantLivraison.setCellValueFactory(
                new PropertyValueFactory<>("idIntersectionLivraison"));
        columnPlageHoraireLivraison.setCellValueFactory(
                new PropertyValueFactory<>("plageHoraireLivraison"));
        columnHeure.setCellValueFactory(
                new PropertyValueFactory<>("heureAffiche"));
        columnLivreur.setCellValueFactory(
                new PropertyValueFactory<>("livreur"));
        columnPlageHoraireLivraison.setCellFactory(
                new Callback<TableColumn<Livraison, PlageHoraire>, TableCell<Livraison, PlageHoraire>>() {
                    @Override
                    public TableCell<Livraison, PlageHoraire> call(TableColumn<Livraison, PlageHoraire> param) {
                        final TableCell<Livraison, PlageHoraire> tableCell = new TableCell<Livraison, PlageHoraire>() {
                            @Override public void updateItem(PlageHoraire plageHoraire, boolean empty) {
                                super.updateItem(plageHoraire, empty);
                                if (plageHoraire != null) {
                                    setText(plageHoraire.toString());
                                    setTextFill(plageHoraire.getCouleur());
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return tableCell;
                    }
                });
        tableViewLivraisons.setRowFactory(tv -> new TableRow<Livraison>() {
            @Override
            protected void updateItem(Livraison liv, boolean empty) {
                super.updateItem(liv, empty);
                if (liv == null)
                    setStyle("");
                else if (!liv.isDansSaPlageHorraire())
                    setStyle("-fx-background-color: #ffaea8;");
                else
                    setStyle("");
            }
        });



        comboboxLivreur.getItems().add(1);
        comboboxLivreur.getSelectionModel().selectFirst();

        for(int i=8; i<12; i++) {
            comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
        }
        comboboxPlageHoraire.setCellFactory(
                new Callback<ListView<PlageHoraire>, ListCell<PlageHoraire>>() {
                    @Override public ListCell<PlageHoraire> call(ListView<PlageHoraire> param) {
                        final ListCell<PlageHoraire> cell = new ListCell<PlageHoraire>() {
                            @Override public void updateItem(PlageHoraire plageHoraire, boolean empty) {
                                super.updateItem(plageHoraire, empty);
                                if (plageHoraire != null) {
                                    setText(plageHoraire.toString());
                                    setTextFill(plageHoraire.getCouleur());
                                }
                                else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                });
    }

    private void actionBoutonAfficherFeulleDeRoute(ActionEvent event) {
        vue.FenetreFeuilleDeRoute.display(controleur, this.comboboxLivreur.getValue());
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

    private void actionBoutonAssignerNvLivreur(ActionEvent event) {
        controleur.ajouterLivreur();
    }

    private void actionBoutonChargerPlan(ActionEvent event) throws Exception {
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
    private void actionBoutonChargerDemande(ActionEvent event) throws Exception{
        controleur.chargerListeDemandes();
    }

    private void actionBoutonCalculerTournees(ActionEvent event) {

        controleur.calculerTournees();
        tableViewDemandesLivraison.setVisible(false);
        tableViewLivraisons.setVisible(true);
    }

    public void afficherDemandeLivraison(boolean nettoyerCanvas) {
        GraphicsContext gc = canvasIntersectionsLivraisons.getGraphicsContext2D();
        if(nettoyerCanvas){
            gc.clearRect(0, 0, canvasIntersectionsLivraisons.getWidth(), canvasIntersectionsLivraisons.getHeight());
        }

        for(DemandeLivraison d: controleur.getJournee().getDemandesLivraison()) {
            this.dessinerIntersection(gc,
                    d.getIntersection(),
                    d.getPlageHoraire().getCouleur(),
                    this.TAILLE_RECT_PT_LIVRAISON,
                    true,
                    FormeIntersection.RECTANGLE);
        }

    }

    public void afficherLivraison(boolean nettoyerCanvas){
        GraphicsContext gc = canvasIntersectionsLivraisons.getGraphicsContext2D();
        if(nettoyerCanvas){
            gc.clearRect(0, 0, canvasIntersectionsLivraisons.getWidth(), canvasIntersectionsLivraisons.getHeight());
        }

        for(Livraison l: controleur.getJournee().getLivraisonsLivreur(comboboxLivreur.getValue())) {
            this.dessinerIntersection(gc,
                    l.getDemandeLivraison().getIntersection(),
                    l.getDemandeLivraison().getPlageHoraire().getCouleur(),
                    this.TAILLE_RECT_PT_LIVRAISON,
                    true,
                    FormeIntersection.RECTANGLE);
        }
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
     * @param intersection intersection à dessiner
     * @param couleur couleur de l'intersection
     * @param remplir précise s'il faut remplir l'intersection
     * @param forme RECTANGLE ou CERCLE
     */
    public void dessinerIntersection(GraphicsContext gc,
                                     Intersection intersection,
                                     Color couleur,
                                     double taille,
                                     boolean remplir,
                                     FormeIntersection forme) {
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
     * @param x coordonnées x sur le canvas
     * @param y coordonnées y sur le canvas
     * @param couleur couleur de l'intersection
     * @param remplir true si le point doit être plein
     * @param forme RECTANGLE, CERCLE
     */
    void dessinerIntersectionXY(GraphicsContext gc,
                                double x,
                                double y,
                                Color couleur,
                                double taille,
                                boolean remplir,
                                FormeIntersection forme) {
        if(remplir) {
            gc.setFill(couleur);
            switch(forme) {
                case RECTANGLE:
                    gc.fillRect(x - (taille /2),
                            y - (taille /2),
                            taille,
                            taille);
                    break;
                case CERCLE:
                    gc.fillOval(x - (taille /2),
                            y - (taille /2),
                            taille,
                            taille);
                    break;
            }
        } else {
            gc.setStroke(couleur);
            switch(forme) {
                case RECTANGLE:
                    gc.strokeRect(x - (taille /2),
                            y - (taille /2),
                            taille,
                            taille);
                    break;
                case CERCLE:
                    gc.strokeOval(x - (taille /2),
                            y - (taille /2),
                            taille,
                            taille);
                    break;
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

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Journee) {
            tableViewDemandesLivraison.getItems().clear();
            tableViewDemandesLivraison.getItems().addAll(
                    ((Journee) o).getDemandesLivraison());
            afficherDemandeLivraison(true);
        }
        else if(arg == "ChangementLivraison"){
            System.out.println("update tableau");
            tableViewLivraisons.refresh();
            afficherLivraison(true);
        }
    }

    public void dessinerTrajet(List<Trajet> trajets, GraphicsContext gc) {
        for (Trajet trajet : trajets) {
            List<Segment> segments = trajet.getSegments();
            for (Segment segment : segments) {
                dessinerTrajetLatLong(gc, segment.getOrigine().getLatitude(),
                        segment.getOrigine().getLongitude(),
                        segment.getDestination().getLatitude(),
                        segment.getDestination().getLongitude());
            }
        }
    }
}
