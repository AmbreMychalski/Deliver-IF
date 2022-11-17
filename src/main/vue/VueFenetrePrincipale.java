package vue;


import controleur.ControleurFenetrePrincipale;
import exception.FichierNonConformeException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import modele.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static controleur.ControleurFenetrePrincipale.LOGGER;

public class VueFenetrePrincipale implements Observer {
    ControleurFenetrePrincipale controleur;

    public final double TAILLE_RECT_PT_LIVRAISON = 10;
    public final double TAILLE_RECT_PT_LIVRAISON_SELECTIONNE = 14;
    public final double TAILLE_CERCLE_INTERSECTION_SELECTIONNEE = 10;
    public final double TAILLE_CERCLE_INTERSECTION = 14;
    public final Color COULEUR_DEPOT = Color.RED;
    public final Color COULEUR_SEGMENT = Color.BLACK;
    public final Color COULEUR_POINT_LIVRAISON_SELECTIONNE = Color.RED;
    /* Rayon en pixels définissant la zone où l'on
     reconnaît les intersections ciblées*/
    public final double RAYON_TOLERANCE_CLIC_INTERSECTION = 8;
    public enum FormeIntersection { RECTANGLE, CERCLE }

    @Getter
    @Setter
    Stage stage;
    public float    largeurPlan;
    public float    hauteurPlan;
    public double   echelleLong;
    public double   echelleLat;
    public double   dernierePositionX = 0;
    public double   dernierePositionY = 0;
    public double   decalageX = 0;
    public double   decalageY = 0;
    public double   positionCouranteX = 0;
    public double   positionCouranteY = 0;


    public Float latMax;
    public Float latMin;
    public Float longMax;
    public Float longMin;

    // objets FXML
    @FXML
    public AnchorPane anchorPaneSelectionDemande;
    @FXML
    public Button   buttonValiderLivraison;
    @FXML
    public Button   buttonAnnulerLivraison;
    @FXML
    public Button   buttonAutoriserAjouterLivraison;
    @FXML
    public Button   buttonCalculerTournees;
    @FXML
    public Button   buttonSauvegarderDemandes;
    @FXML
    public Button   buttonChargerDemandes;
    @FXML
    public Button   buttonAfficherFeuillesRoute;
    @FXML
    public Button buttonReinitAffPlan;
    @FXML
    public Button   buttonChargerPlan;
    @FXML
    public Button   buttonSupprimerLivraison;
    @FXML
    public Button   buttonModifierLivraison;
    @FXML
    public Button   buttonAssignerNvLivreur;
    @FXML
    public TableView<DemandeLivraison>  tableViewDemandesLivraison;
    @FXML
    public TableView<Livraison>         tableViewLivraisons;
    @FXML
    public TableColumn<DemandeLivraison, Long>      columnIdentifiant;
    @FXML
    public TableColumn<DemandeLivraison, PlageHoraire> columnPlageHoraire;
    @FXML
    public TableColumn<Livraison, Long>             columnIdentifiantLivraison;
    @FXML
    public TableColumn<Livraison, PlageHoraire>     columnPlageHoraireLivraison;
    @FXML
    public TableColumn<Livraison, String>           columnHeure;
    @FXML
    public Canvas   canvasPlan;
    @FXML
    public Canvas   canvasIntersectionsLivraisons;
    @FXML
    public Canvas   canvasPlanTrajet;
    @FXML
    public ComboBox<PlageHoraire>   comboboxPlageHoraire;
    @FXML
    public ComboBox<Livreur>        comboboxLivreurNouvelleDemande;
    @FXML
    public ComboBox<Livreur>        comboboxLivreur;
    @FXML
    public ComboBox<String>         comboboxAssignerLivreur;
    @FXML
    public TextField    textfieldIdentifiantIntersection;
    @FXML
    public TextField    textfieldPlageHoraire;
    @FXML
    public TextField    textfieldIdentifiantIntersectionSelection;
    @FXML
    public TitledPane   titlePaneSelectionDemande;
    @FXML
    public TitledPane   titledPaneEditionDemande;
    @FXML
    public Label labelGuideUtilisateur;
    @FXML
    public Label labelRuesIntersection;
    @FXML
    public Label labelLivreurNouvelleDemande;

    @FXML
    private void initialize() {
        controleur = new ControleurFenetrePrincipale(this);

        buttonSupprimerLivraison.setOnAction(this::actionBoutonSupprimerLivraison);
        buttonModifierLivraison.setOnAction(this::actionBoutonModifierLivraison);
        buttonAssignerNvLivreur.setOnAction(this::actionBoutonAssignerNvLivreur);

        comboboxLivreurNouvelleDemande.setVisible(false);
        labelLivreurNouvelleDemande.setVisible(false);


        titlePaneSelectionDemande.setVisible(false);
        titledPaneEditionDemande.setVisible(false);
        tableViewLivraisons.setVisible(false);

        buttonAfficherFeuillesRoute.setOnAction(this::actionBoutonAfficherFeulleDeRoute);
        buttonValiderLivraison.setOnAction(this::actionBoutonAjouterLivraison);
        buttonAnnulerLivraison.setOnAction(this::actionBoutonAnnulerLivraison);
        buttonAutoriserAjouterLivraison.setOnAction(this::actionBoutonAutoriserAjouterLivraison);
        comboboxLivreur.setOnAction(event -> actionClicSurLivreur());
        comboboxAssignerLivreur.setOnAction(event -> actionClicComboboxAssisgnerLivreur());
        buttonChargerDemandes.setOnAction(event -> {
            try{
                actionBoutonChargerDemande(event);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                labelGuideUtilisateur.setText("Problème lors du chargement du fichier");
            }
        });
        buttonSauvegarderDemandes.setOnAction(this::actionBoutonSauvegarderDemandes);
        canvasIntersectionsLivraisons.setOnMouseClicked(this::actionClicSurCanvas);
        tableViewDemandesLivraison.setOnMouseClicked(this::actionClicTableau);
        tableViewLivraisons.setOnMouseClicked(this::actionClicTableau);
        buttonReinitAffPlan.setOnAction(this::actionButtonReinitZoomPlan);
        buttonReinitAffPlan.setVisible(false);
        buttonChargerPlan.setOnAction(event -> {
            try {
                actionBoutonChargerPlan(event);
            } catch (Exception e) {
                LOGGER.error(e);
                labelGuideUtilisateur.setText("Impossible de charger le plan");
            }
        });
        buttonCalculerTournees.setOnAction(this::actionBoutonCalculerTournees);


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
                    return new TableCell<DemandeLivraison, PlageHoraire>() {
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
                }
        });

        tableViewLivraisons.setItems(FXCollections.observableArrayList());
        columnIdentifiantLivraison.setCellValueFactory(
                new PropertyValueFactory<>("idIntersectionLivraison"));
        columnPlageHoraireLivraison.setCellValueFactory(
                new PropertyValueFactory<>("plageHoraireLivraison"));
        columnPlageHoraireLivraison.setComparator(new ComparateurPlageHoraire());

        columnHeure.setCellValueFactory(
                new PropertyValueFactory<>("heureAffichee"));
        columnHeure.setComparator(new ComparateurHeureLivraison());
        columnPlageHoraireLivraison.setCellFactory(
                new Callback<TableColumn<Livraison, PlageHoraire>, TableCell<Livraison, PlageHoraire>>() {
                    @Override
                    public TableCell<Livraison, PlageHoraire> call(TableColumn<Livraison, PlageHoraire> param) {
                        return new TableCell<Livraison, PlageHoraire>() {
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
                    }
                });
        tableViewLivraisons.setRowFactory(tv -> new TableRow<Livraison>() {
            @Override
            protected void updateItem(Livraison liv, boolean empty) {
                super.updateItem(liv, empty);
                if (liv == null)
                    setStyle("");
                else if (!liv.isDansSaPlageHoraire())
                    setStyle("-fx-background-color: #ffaea8;");
                else
                    setStyle("");
            }
        });
        for(int i=8; i<12; i++) {
            comboboxPlageHoraire.getItems().add(new PlageHoraire(i,i+1));
        }
        comboboxPlageHoraire.setCellFactory(
                new Callback<ListView<PlageHoraire>, ListCell<PlageHoraire>>() {
                    @Override public ListCell<PlageHoraire> call(ListView<PlageHoraire> param) {
                        return new ListCell<PlageHoraire>() {
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
                    }
                });

        canvasIntersectionsLivraisons.setOnMouseDragged(this::actionDeplacerPlan);
        canvasIntersectionsLivraisons.setOnMousePressed(event -> {
            dernierePositionY = event.getY();
            dernierePositionX = event.getX();
        });
        canvasIntersectionsLivraisons.setOnMouseMoved(event -> {
            positionCouranteY = event.getY();
            positionCouranteX = event.getX();
        });

        List<Control> controles = obtenirControlsVue();

        for(Control controle : controles) {
                if(controle instanceof Button || controle instanceof TableView) {
                controle.setOnMouseEntered(event -> {
                    if (!controle.isDisable()) {
                        this.getStage().getScene().setCursor(Cursor.HAND);
                    }
                });

                controle.setOnMouseExited(event ->
                        this.getStage().getScene().setCursor(Cursor.DEFAULT));
            }
        }
    }

    private void actionButtonReinitZoomPlan(ActionEvent actionEvent) {
        dessinerPlan();

        if(comboboxLivreur.getValue().getTournee() == null) {
            afficherDemandesLivraison(comboboxLivreur.getValue(), true);
        } else {
            afficherLivraisons(comboboxLivreur.getValue(), true);
        }
    }

    public File choisirFichier(String titreFenetre) throws Exception {
        File fichier;

        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML",
                        "*.xml", "*.XML"));
        fileChooser.setTitle(titreFenetre);

        try {
            fichier = fileChooser.showOpenDialog(this.stage);

            if(fichier == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            throw new FichierNonConformeException("Problème lors du choix du fichier.");
        }
        return fichier;
    }

    public void nettoyerToutesLesInformations(ControleurFenetrePrincipale c) {
        GraphicsContext gc = canvasPlan.getGraphicsContext2D();
        GraphicsContext gcTrajet = canvasPlanTrajet.getGraphicsContext2D();
        GraphicsContext gcIntersection = canvasIntersectionsLivraisons.getGraphicsContext2D();

        gc.clearRect(0, 0, canvasPlan.getWidth(), canvasPlan.getHeight());
        gcTrajet.clearRect(0, 0, canvasPlanTrajet.getWidth(), canvasPlanTrajet.getHeight());
        gcIntersection.clearRect(0, 0, canvasIntersectionsLivraisons.getWidth(), canvasIntersectionsLivraisons.getHeight());
        textfieldIdentifiantIntersectionSelection.setText("");
        textfieldPlageHoraire.setText("");
    }

    private void actionDeplacerPlan(MouseEvent event) {
        double x =  event.getX();
        double y = event.getY();
        decalageX += x - dernierePositionX;
        decalageY += y - dernierePositionY;
        dernierePositionY = y;
        dernierePositionX = x;

        redessinerPlan(false, 0);

        if(comboboxLivreur.getValue().getTournee() == null) {
            afficherDemandesLivraison(comboboxLivreur.getValue(), true);
        } else {
            afficherLivraisons(comboboxLivreur.getValue(), true);
        }
    }

    private void actionClicComboboxAssisgnerLivreur() {
        controleur.actionClicComboboxAssignerLivreur();
    }

    private void actionBoutonAfficherFeulleDeRoute(ActionEvent event) {
        if(this.comboboxLivreur.getValue() != null){
            vue.FenetreFeuilleDeRoute.display(controleur, this.comboboxLivreur.getValue());
        } else {
            vue.FenetreFeuilleDeRoute.display(controleur, controleur.getJournee().getLivreurs().get(0));
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

    private void actionBoutonAssignerNvLivreur(ActionEvent event) {
        controleur.assignerAutreLivreur();
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

    public void afficherDemandesLivraison(Livreur livreur, boolean nettoyerCanvas) {
        GraphicsContext gc = canvasIntersectionsLivraisons.getGraphicsContext2D();
        GraphicsContext gcTrajet = canvasPlanTrajet.getGraphicsContext2D();

        if(nettoyerCanvas) {
            gc.clearRect(0, 0, canvasIntersectionsLivraisons.getWidth(),
                    canvasIntersectionsLivraisons.getHeight());
            gcTrajet.clearRect(0, 0, canvasPlanTrajet.getWidth(),
                    canvasPlanTrajet.getHeight());
        }

        for(DemandeLivraison d: livreur.getDemandeLivraisons()) {
            this.dessinerDemandeLivraison(gc, d);
        }
    }

    public void afficherLivraisons(Livreur livreur, boolean nettoyerCanvas) {
        GraphicsContext gc = canvasIntersectionsLivraisons.getGraphicsContext2D();
        GraphicsContext gcTrajets = canvasPlanTrajet.getGraphicsContext2D();

        if (nettoyerCanvas) {
            gc.clearRect(0, 0, canvasIntersectionsLivraisons.getWidth(), canvasIntersectionsLivraisons.getHeight());
            gcTrajets.clearRect(0, 0, canvasPlanTrajet.getWidth(), canvasPlanTrajet.getHeight());
        }

        List<Livraison> livraisons;

        if(livreur.getTournee() != null) {
            livraisons = livreur.getTournee().getLivraisons();

            for (Livraison l : livraisons) {
                this.dessinerLivraison(gc, l);
            }

            dessinerDemandeLivraison(gc, livreur.getDemandeLivraisons()
                    .get(livreur.getDemandeLivraisons().size()-1));
            dessinerTrajets(livreur.getTournee().getTrajets(), gcTrajets);
        }
    }

    public void dessinerDemandeLivraison(GraphicsContext gc, DemandeLivraison demande) {
        dessinerIntersection(gc,
                demande.getIntersection(),
                demande.getPlageHoraire().getCouleur(),
                this.TAILLE_RECT_PT_LIVRAISON,
                true,
                FormeIntersection.RECTANGLE
        );
    }

    private void dessinerLivraison(GraphicsContext gc, Livraison l) {
        this.dessinerIntersection(gc,
                l.getDemandeLivraison().getIntersection(),
                l.getDemandeLivraison().getPlageHoraire().getCouleur(),
                this.TAILLE_RECT_PT_LIVRAISON,
                true,
                FormeIntersection.RECTANGLE
        );
    }

    private void actionBoutonAjouterLivraison(ActionEvent event) {
        controleur.validerAjouterOuModifier();
    }

    private void actionClicSurLivreur() {
        LOGGER.debug("appel a action clic livreur");
        controleur.clicSurLivreur();
    }

    /**
     * Action à effectuer lors du clic sur le bouton Sauvegarder demandes.
     * Ouvre un explorateur de fichier pour choisir l'emplacement du fichier.
     * @param event ActionEvent associé
     */
    private void actionBoutonSauvegarderDemandes(ActionEvent event) {
        controleur.sauvegarderDemandes();
    }

    public void updateLabelGuideUtilisateur(String texte) {
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
     * @param couleur couleur du segment
     */
    public void dessinerSegment(Segment segment, Color couleur) {
        dessinerSegmentXY(convertirLongitudeEnX(segment.getOrigine().getLongitude()),
                convertirLatitudeEnY(segment.getOrigine().getLatitude()),
                convertirLongitudeEnX(segment.getDestination().getLongitude()),
                convertirLatitudeEnY(segment.getDestination().getLatitude()),
                couleur);
    }


    public void dessinerTrajetLatLong(GraphicsContext gc, LinearGradient color,
                                      double lat1, double long1, double lat2,
                                      double long2, int nombreOccurence,
                                      boolean dejaDessine) {
        dessinerSegmentGradientXY(gc, color, convertirLongitudeEnX(long1),
                convertirLatitudeEnY(lat1),
                convertirLongitudeEnX(long2),
                convertirLatitudeEnY(lat2),
                nombreOccurence,
                dejaDessine);
    }

    public void dessinerSurbrillanceSegment(GraphicsContext gc, double lat1,
                                            double long1, double lat2,
                                            double long2) {
        gc.setLineWidth(4);
        gc.setStroke(Color.GREEN);
        gc.strokeLine(convertirLongitudeEnX(long1),
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
     * @param couleur couleur du segment
     */
    void dessinerSegmentXY(double x1, double y1, double x2, double y2,
                           Color couleur) {
        GraphicsContext gc = canvasPlan.getGraphicsContext2D();

        gc.setStroke(couleur);
        gc.strokeLine(x1, y1, x2, y2);
    }

    private void dessinerSegmentGradientXY(GraphicsContext gc, LinearGradient color,
                                           double x1, double y1, double x2,
                                           double y2, int nombreOccurence,
                                           boolean dejaDessine) {
        gc.setStroke(color);

        if(nombreOccurence > 1) {
            gc.setLineDashes(20);
            gc.setLineDashOffset(dejaDessine ? 20 : 0);
            gc.setLineWidth(5);
        } else {
            gc.setLineWidth(4);
            gc.setLineDashes();
        }

        gc.strokeLine(x1, y1, x2, y2);
    }

    /**
     * Convertit une longitude en pixels sur le Canvas (axe X).
     * @param x longitude
     * @return coordonnée X sur le Canvas
     */
    double convertirLongitudeEnX(double x) {
        return decalageX + (x - this.longMin) * this.echelleLong;
    }

    /**
     * Convertit une latitude en pixels sur le Canvas (axe Y).
     * @param y latitude
     * @return coordonnée Y sur le Canvas
     */
    double convertirLatitudeEnY(double y) {
        // quand on retourne la carte, elle n'est pas calée en haut

        // on la cale donc en lui retirant "aRemonter"
        double aRemonter = this.canvasPlan.getHeight()
                - (this.latMax - this.latMin) * this.echelleLat;
        double latitudeYPx = (this.canvasPlan.getHeight() - (y - this.latMin)
                * this.echelleLat);

        return  decalageY + latitudeYPx - aRemonter;

    }

    @Override
    public void update(Observable o, Object arg) {
        Livreur livreur = comboboxLivreur.getValue();
        if(o instanceof Journee) {
            if (arg == "AjoutLivreur") {
                controleur.getEtatCourant().majComboboxLivreur(controleur); //pas sur que ce soit légal
            }
        } else if(o instanceof Livreur) {
            if(arg == "ModificationAjoutSuppressionDemandeLivraison") {
                tableViewDemandesLivraison.getItems().clear();
                tableViewDemandesLivraison.getItems().addAll(
                        ((Livreur) o).getDemandeLivraisons());
                afficherDemandesLivraison(livreur, true);
            }else if(arg == "SuppressionTournee") {
                    tableViewLivraisons.getItems().clear();
                    tableViewDemandesLivraison.setVisible(true);
                    tableViewLivraisons.setVisible(false);
                    canvasIntersectionsLivraisons.getGraphicsContext2D().clearRect(0, 0,
                            canvasIntersectionsLivraisons.getWidth(),
                            canvasIntersectionsLivraisons.getHeight());
            } else if (arg == "ModificationTournee") {
                tableViewLivraisons.getItems().clear();
                tableViewLivraisons.getItems().addAll(
                        ((Livreur) o).getLivraisons());
                afficherLivraisons(livreur, true);
            }
        }
    }

    private boolean segmentOuInverseDejaDessines(List<Segment> segmentsDessines,
                                                 Segment segment) {
        for(Segment segment1 : segmentsDessines) {
            if(segment1.equals(segment)) {
                return true;
            }if(segment1.getDestination() == segment.getOrigine()
                    && segment.getDestination() == segment1.getOrigine()) {
                return true;
            }
        }

        return false;
    }

    private int obtenirNombreSegmentSuperpose(List<Trajet> tournee, Segment segment) {
        int res = 0;

        for(Trajet trajet: tournee) {
            for (Segment segment1 : trajet.getSegments()) {
                if (segment.equals(segment1)
                        || segment1.getDestination() == segment.getOrigine()
                        && segment.getDestination() == segment1.getOrigine()) {
                    res++;
                }
            }
        }

        return res;
    }

    public void dessinerTrajets(List<Trajet> trajets, GraphicsContext gc) {
        int size = 0;
        List<Segment> segmentsDessines = new ArrayList<>();

        for(Trajet trajet : trajets) {
            size += trajet.getSegments().size();
        }

        size++;
        List<Color> couleurs = new ArrayList<>();

        for(double i = 0; i< size; i++) {
            if(i < size/3) {
                couleurs.add(new Color(0.5, Math.max(0.35, 1-2*i/size), 1, 1));
            } else if (i >= size / 3 && i < 2 * size / 3) {
                couleurs.add(new Color((3.0 / 2 * i / size),0.35, 1, 1));
            } else {
                couleurs.add(new Color(1,0.35, 1 - 3.0 * ((i - (2.0 * size / 3) + 1) / size), 1));
            }
        }

        List<LinearGradient> linearGradients = new ArrayList<>();

        for(int i = 1; i< size; i++) {
            Stop[] stops = new Stop[] {
                    new Stop(0, couleurs.get(i-1)),
                    new Stop(1, couleurs.get(i))
            };
            linearGradients.add(new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE, stops));
        }

        int i = 0;

        for (Trajet trajet : trajets) {
            List<Segment> segments = trajet.getSegments();

            for (Segment segment : segments) {
                dessinerTrajetLatLong(gc, linearGradients.get(i),
                        segment.getOrigine().getLatitude(),
                        segment.getOrigine().getLongitude(),
                        segment.getDestination().getLatitude(),
                        segment.getDestination().getLongitude(),
                        obtenirNombreSegmentSuperpose(trajets, segment),
                        segmentOuInverseDejaDessines(segmentsDessines, segment));
                segmentsDessines.add(segment);
                i++;
            }
        }

        gc.setLineDashes();

        double width = 100;
        int height = 20;
        int x_shift = 150;
        int y_shift = 20;

        size--;

        for(i = 0; i < size; i++) {
            gc.setFill(linearGradients.get(i));
            gc.fillRect(x_shift + i*(width/size) , y_shift, width/size, height);
        }
    }

    public void dessinerSurbrillanceTrajet(Trajet trajet, GraphicsContext gc) {
        List<Segment> segments = trajet.getSegments();
        for (Segment segment : segments) {
            dessinerSurbrillanceSegment(gc, segment.getOrigine().getLatitude(),
                    segment.getOrigine().getLongitude(),
                    segment.getDestination().getLatitude(),
                    segment.getDestination().getLongitude());
        }
    }

    /**
     * Donne la liste de tous les Controls de la vue (boutons, combobox, etc)
     * SAUF les TableView, TitlePane, Label
     * @return liste des Controls
     */
    public ArrayList<Control> obtenirControlsVue() {
        Field [] attributes = this.getClass().getDeclaredFields();
        ArrayList<Control> controls = new ArrayList<>();

        for (Field attribute : attributes) {
            try {
                if (attribute.get(this) instanceof Control
                        && !(attribute.get(this) instanceof TableView)
                        && !(attribute.get(this) instanceof TitledPane)
                        && !(attribute.get(this) instanceof Label)) {
                    controls.add((Control) attribute.get(this));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return controls;
    }

    public void activerExclusivementBoutons(ArrayList<Control> controlsActives) {
        ArrayList<Control> tousLesControls = obtenirControlsVue();
        for (Control b: tousLesControls) {
            b.setDisable(!controlsActives.contains(b));
        }
    }

    public void dessinerPlan() {
        latMax = controleur.getJournee().getPlan().getIntersections().values().stream()
                .map(Intersection::getLatitude)
                .max(Float::compare).orElse(0f);
        latMin = controleur.getJournee().getPlan().getIntersections().values().stream()
                .map(Intersection::getLatitude)
                .min(Float::compare).orElse(0f);
        longMax = controleur.getJournee().getPlan().getIntersections().values().stream()
                .map(Intersection::getLongitude)
                .max(Float::compare).orElse(0f);
        longMin = controleur.getJournee().getPlan().getIntersections().values().stream()
                .map(Intersection::getLongitude)
                .min(Float::compare).orElse(0f);

        largeurPlan = longMax - longMin;
        hauteurPlan = latMax - latMin;

        echelleLong = canvasPlan.getWidth() / largeurPlan;
        echelleLat = canvasPlan.getHeight() / hauteurPlan;

        decalageX = 0;
        decalageY = 0;

        canvasPlan.getGraphicsContext2D().clearRect(0,0, canvasPlan.getWidth(),
                canvasPlan.getHeight());

        for (Segment segment : controleur.getJournee().getPlan().getSegments()) {
            dessinerSegment(segment, COULEUR_SEGMENT);
        }

        dessinerIntersection(canvasPlan.getGraphicsContext2D(),
                controleur.getJournee().getPlan().getEntrepot(),
                COULEUR_DEPOT,
                TAILLE_CERCLE_INTERSECTION,
                true,
                VueFenetrePrincipale.FormeIntersection.CERCLE);
    }

    public void redessinerPlan(boolean miseAEchelle, double echelleGlobale) {
        if(miseAEchelle) {
            this.echelleLat *= echelleGlobale;
            this.echelleLong *= echelleGlobale;
            this.decalageX = this.decalageX*echelleGlobale + this.positionCouranteX
                    - (this.positionCouranteX * echelleGlobale);
            this.decalageY = this.decalageY*echelleGlobale + this.positionCouranteY
                    - (this.positionCouranteY * echelleGlobale);
        }

        canvasPlan.getGraphicsContext2D().clearRect(0, 0, canvasPlan.getWidth(),
                canvasPlan.getHeight());

        for (Segment segment : controleur.getJournee().getPlan().getSegments()) {
            dessinerSegment(segment, COULEUR_SEGMENT);
        }
        dessinerIntersection(canvasPlan.getGraphicsContext2D(),
                controleur.getJournee().getPlan().getEntrepot(),
                COULEUR_DEPOT,
                TAILLE_CERCLE_INTERSECTION,
                true,
                VueFenetrePrincipale.FormeIntersection.CERCLE);
    }
}
