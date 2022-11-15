package controleur;

import exception.FichierNonConformeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import lombok.Getter;
import modele.*;
import vue.VueFenetrePrincipale;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static controleur.ControleurFenetrePrincipale.LOGGER;

@Getter
public abstract class Etat {
	protected String message;
	public Etat() {
		this.message = "Etat";
	}

	public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {}
	
	public void ajouterDemande(ControleurFenetrePrincipale c){}
	
	public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event){}
	
	public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}

	public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception {}
	
	public void supprimerDemande(ControleurFenetrePrincipale c) {}
	
	public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
	
	public void calculerTournees(ControleurFenetrePrincipale c) {}
	
	public void modifierDemande(ControleurFenetrePrincipale c) {}

	public void assignerAutreLivreur(ControleurFenetrePrincipale c) {}

	public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
		LOGGER.info(ke.getCode());
		if(! c.vue.comboboxLivreur.isDisable()){
			switch(ke.getCode()) {
				case Q:
					c.vue.comboboxLivreur.getSelectionModel().selectPrevious();
					this.changementLivreur(c);
					break;
				case D:
					c.vue.comboboxLivreur.getSelectionModel().selectNext();
					this.changementLivreur(c);
					break;
				case P:
				case ADD:
					c.vue.redessinerPlan(true,1.5);
					if(c.vue.comboboxLivreur.getValue().getTournee() != null){
						c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
					} else {
						c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(), true);
					}
					break;
				case M:
				case SUBTRACT:
					c.vue.redessinerPlan(true,0.66);
					if(c.vue.comboboxLivreur.getValue().getTournee() != null){
						c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
					} else {
						c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(), true);
					}
					break;
			}
		}
	}


	public void clicSurLivreur(ControleurFenetrePrincipale c) {}
	public void clicSurComboboxAssignerLivreur(ControleurFenetrePrincipale controleurFenetrePrincipale) {
	}

	protected void sortieDeSelectionDemande(ControleurFenetrePrincipale c, boolean livraison){
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		if(livraison){
			c.vue.buttonAssignerNvLivreur.setDisable(true);
			if(c.vue.comboboxLivreur.getValue() == null){
				c.vue.afficherLivraisons(c.journee.getLivreurs().get(0), true);
			}else{
				c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(),true);
			}
		}
		else{
			c.vue.afficherDemandesLivraison(livreur, true);
		}
		c.vue.textfieldIdentifiantIntersectionSelection.setText("");
		resetLabelRuesIntersection(c);
		c.vue.textfieldPlageHoraire.setText("");
	}

	protected  void afficherTournee(ControleurFenetrePrincipale c, Tournee tournee){
		GraphicsContext gc = c.vue.canvasPlanTrajet.getGraphicsContext2D();
		gc.clearRect(0, 0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
		if(tournee != null){
			List<Trajet> trajets = tournee.getTrajets();
			c.vue.dessinerTrajets(trajets, gc);
		}
	}

	protected void sauvegarderListeDemandes(ControleurFenetrePrincipale c){
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.setTitle("Sauvegarder des demandes de livraison");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		File fichier = fileChooser.showSaveDialog(c.vue.getStage());

		if (fichier != null) {
			LOGGER.info("Sauvegarde à l'emplacement "
					+ fichier.getAbsolutePath());
			c.journee.sauvegarderDemandesLivraison(fichier, livreur);
		} else {
			LOGGER
					.error("Erreur lors de la sauvegarde des demandes");
		}
	}

	protected void effectuerModification(ControleurFenetrePrincipale c){
		DemandeLivraison ligne;
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		ligne = c.vue.tableViewDemandesLivraison.getSelectionModel()
				.getSelectedItem();
		String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();
		Intersection intersection;
		if(champIdentifiant.isEmpty()) {
			intersection = ligne.getIntersection();
		} else {
			intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
		}
		c.journee.modifierDemandeLivraison(livreur, ligne, intersection, plageHoraire);
		c.vue.dessinerIntersection(
				c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
				ligne.getIntersection(),
				c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
				c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
				true,
				VueFenetrePrincipale.FormeIntersection.RECTANGLE);

		c.vue.textfieldIdentifiantIntersectionSelection.setText(
				ligne.getIdIntersection().toString());
		remplirLabelRuesIntersection(c, intersection);
		c.vue.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
		c.vue.comboboxPlageHoraire.setValue(null);
		c.vue.tableViewDemandesLivraison.setDisable(false);
		c.vue.textfieldIdentifiantIntersection.setText("");
	}

	protected void annulerModification(ControleurFenetrePrincipale c){
		c.vue.comboboxPlageHoraire.setDisable(true);
		c.vue.tableViewDemandesLivraison.setDisable(false);
	}

	public boolean selectionnerDemande(ControleurFenetrePrincipale c, boolean livraison){
		DemandeLivraison ligne = null;
		Livraison liv = null;
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		if(!livraison) {
			ligne = c.vue.tableViewDemandesLivraison.getSelectionModel()
					.getSelectedItem();
		}
		else {
			liv = c.vue.tableViewLivraisons.getSelectionModel()
					.getSelectedItem();
		}
		if(liv != null){
			ligne = liv.getDemandeLivraison();
		}
		if (ligne != null) {
			if(!livraison) {
				c.vue.afficherDemandesLivraison(livreur, true);
			}
			else{
				if(c.vue.comboboxLivreur.getValue() == null){
					c.vue.afficherLivraisons(c.journee.getLivreurs().get(0), true);
				}else{
					c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
				}
				c.vue.comboboxAssignerLivreur.getSelectionModel().select(null);
			}
			c.vue.dessinerIntersection(
					c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
					ligne.getIntersection(),
					c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
					c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
					true,
					VueFenetrePrincipale.FormeIntersection.RECTANGLE);

			c.vue.titlePaneSelectionDemande.setVisible(true);
			c.vue.textfieldIdentifiantIntersectionSelection.setText(
													ligne
													.getIdIntersection()
													.toString());
			remplirLabelRuesIntersection(c, ligne.getIntersection());
			c.vue.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
			return true;
		}
		return false;

	}
	protected  void chargerNouveauPlan(ControleurFenetrePrincipale c) throws Exception{
		File fichier ;
		try {
			fichier = c.vue.choisirFichier("Choisir un plan");
			LOGGER.info("Fichier choisi = " + fichier.getAbsolutePath());
			Plan plan;
			plan = new Plan(fichier);
			if(plan.getEntrepot() != null) {
				c.vue.nettoyerToutesLesInformations(c);
				this.resetLabelRuesIntersection(c);
				if (c.journee.getLivreurs().get(0).getDemandeLivraisons().size() != 0) {
					c.journee = new Journee();
					c.journee.ajouterObservateur(c.vue);
					Livreur.reinitializeNbLivreurs();
					Livreur liv = new Livreur();
					liv.ajouterObservateur(c.vue);
					c.journee.getLivreurs().add(liv);
					c.etatCourant.majComboboxLivreur(c);
					c.vue.comboboxLivreur.getSelectionModel().selectFirst();
				}
				c.journee.setPlan(plan);
				c.vue.dessinerPlan();
				c.vue.titledPaneEditionDemande.setVisible(true);
				c.vue.titlePaneSelectionDemande.setVisible(true);
				c.changementEtat(c.etatSansDemande);
			}
		} catch (Exception ex) {
			throw new FichierNonConformeException("Problème lors de la lecture du fichier");
		}
	}

	protected void selectionTrajet(ControleurFenetrePrincipale c){
		Livraison liv = c.vue.tableViewLivraisons.getSelectionModel()
				.getSelectedItem();

		Livreur livreur = c.vue.comboboxLivreur.getValue();
		Tournee tournee = livreur.getTournee();

		int indexLivr = (tournee.getLivraisons()).indexOf(liv);
		c.vue.dessinerTrajet(tournee.getTrajets().get(indexLivr), c.vue.canvasPlanTrajet.getGraphicsContext2D());
	}
	protected boolean validerAjoutDemande(ControleurFenetrePrincipale c){
		String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		if(!champIdentifiant.isEmpty() && plageHoraire != null) {
			Intersection intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
			if(c.journee.getPlan().estLivrable(intersection)){
				DemandeLivraison demande =
						new DemandeLivraison(intersection, plageHoraire);

				livreur.ajouterDemandeLivraison(demande);

				c.vue.comboboxPlageHoraire.setValue(null);
				c.vue.tableViewDemandesLivraison.setDisable(false);
				c.vue.tableViewLivraisons.setDisable(false);
				c.vue.textfieldIdentifiantIntersection.setText("");
				resetLabelRuesIntersection(c);
				return true;
			}
			else{
				LOGGER.warn("L'intersection n'est pas livrable");
			}
		} else {
			LOGGER.warn("Informations manquantes pour l'ajout de la demande");
		}
		return false;
	}
	protected  Intersection naviguerSurPlan(ControleurFenetrePrincipale c,
									MouseEvent event,
									boolean tourneeCalculee){
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		Intersection intersectionTrouvee = null;
		if (c.journee.getPlan() != null) {
			intersectionTrouvee =
					c.vue.trouverIntersectionCoordoneesPixels(event.getX(),
							event.getY());
			if (intersectionTrouvee != null) {
				c.vue.textfieldIdentifiantIntersection.setText(
						intersectionTrouvee.getIdIntersection().toString());

				GraphicsContext gc = c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D();
				gc.clearRect(0, 0,
						c.vue.canvasIntersectionsLivraisons.getWidth(),
						c.vue.canvasIntersectionsLivraisons.getHeight());
				remplirLabelRuesIntersection(c, intersectionTrouvee);

				if(tourneeCalculee){
					if(c.vue.comboboxLivreur.getValue() == null){
						c.vue.afficherLivraisons(c.journee.getLivreurs().get(0), true);
					}else {
						c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
					}

				} else {
					c.vue.afficherDemandesLivraison(livreur, false);
				}
				c.vue.dessinerIntersection(gc,
						intersectionTrouvee,
						Color.DARKORCHID,
						c.vue.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
						true,
						VueFenetrePrincipale.FormeIntersection.CERCLE);
			} else {
				c.vue.textfieldIdentifiantIntersection.setText("");
				resetLabelRuesIntersection(c);
			}
		} else {
			LOGGER.debug("Clic sur le canvas, (x,y)=("
					+ event.getX() + "," + event.getY() + ")");
		}
		return intersectionTrouvee;
	}

	protected void chargerDemandes(ControleurFenetrePrincipale c)
			throws Exception {
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		File fichier = c.vue.choisirFichier("Charger des demandes de livraison");
		LOGGER.info("Fichier choisi = " + fichier.getAbsolutePath());
		ArrayList<DemandeLivraison> listeDemandes
				= c.journee.chargerDemandesLivraison(fichier,livreur);
		if (listeDemandes.size()==0){
			throw  new Exception("Aucune demande de livraison dans le fichier");
		}
	}

	protected void supprimerDemandeLivraison(ControleurFenetrePrincipale c, Livreur livreur){
		DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel()
				.getSelectedItem();
		if(ligne != null) {
			livreur.supprimerDemandeLivraison(ligne);
			c.vue.textfieldIdentifiantIntersectionSelection.setText("");
			resetLabelRuesIntersection(c);
			c.vue.textfieldPlageHoraire.setText("");

		}
	}
	protected void supprimerLivraison(ControleurFenetrePrincipale c) {
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		Livraison livraisonASupp = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
		if(livraisonASupp != null){
			livreur.supprimerDemandeLivraison(livraisonASupp.getDemandeLivraison());
			c.journee.supprimerLivraisonTournee(livreur, livraisonASupp);
			c.vue.textfieldIdentifiantIntersectionSelection.setText("");
			c.vue.textfieldPlageHoraire.setText("");

			this.sortieDeSelectionDemande(c,true);
			if(livreur.getTournee() != null){
				c.changementEtat(c.etatTourneesCalculees);
			} else {
				c.vue.canvasPlanTrajet.getGraphicsContext2D().clearRect(0,0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
				c.changementEtat(c.etatSansDemande);
			}
		}
	}

	protected  void annulerAjout (ControleurFenetrePrincipale c){
		c.vue.tableViewDemandesLivraison.setDisable(false);
		c.vue.tableViewLivraisons.setDisable(false);
		c.vue.textfieldIdentifiantIntersection.setText("");
		resetLabelRuesIntersection(c);
		c.vue.comboboxPlageHoraire.setValue(null);
	}
	protected void remplirLabelRuesIntersection(ControleurFenetrePrincipale c,
												Intersection intersection){
		List<String> rues = c.getJournee().getPlan().obtenirRuesIntersection(intersection);
		String texte;
		if((rues.get(0) == null || rues.get(0).isEmpty())
				&& (rues.get(1) == null || rues.get(1).isEmpty())){
			texte = "Aucune rue associée";
		}else if(rues.get(1) == null || rues.get(1).isEmpty()){
			texte = rues.get(0);
		}else if(rues.get(0) == null|| rues.get(0).isEmpty()){
			texte = rues.get(1);
		}else {
			texte = "Croisement \n "+rues.get(0) + " \n et \n"+ rues.get(1);
		}
		c.vue.labelRuesIntersection.setText(texte);
	}

	protected void resetLabelRuesIntersection(ControleurFenetrePrincipale c){
		c.vue.labelRuesIntersection.setText("Aucune intersection selectionnée");
	}

	public void majComboboxLivreur(ControleurFenetrePrincipale c) {
		ObservableList<Livreur> listeLivreur = FXCollections.observableArrayList();
		ObservableList<String> listStr = FXCollections.observableArrayList();
		listeLivreur.addAll(c.journee.getLivreurs());
		for (int i = 1; i <= c.journee.getLivreurs().size(); i++) {
			listStr.add(Integer.toString(i));
		}
		if(!c.journee.dernierLivreurEstSansTourneeCalculee()){
			listStr.add(c.journee.getLivreurs().size()+1+" (nouveau livreur)");
		}
		c.vue.comboboxAssignerLivreur.setItems(listStr);
		c.vue.comboboxLivreur.setItems(listeLivreur);
	}

	protected void miseAjourDonneesTableView(ControleurFenetrePrincipale c, Livreur livreur){
		c.vue.tableViewLivraisons.getItems().clear();
		c.vue.tableViewDemandesLivraison.getItems().clear();
		if(livreur.getTournee() != null){
			c.vue.tableViewLivraisons.setVisible(true);
			c.vue.tableViewDemandesLivraison.setVisible(false);
			c.vue.tableViewLivraisons.getItems().addAll(livreur.getTournee().getLivraisons());
		}else{
			c.vue.tableViewLivraisons.setVisible(false);
			c.vue.tableViewDemandesLivraison.setVisible(true);
			c.vue.tableViewDemandesLivraison.getItems().addAll(livreur.getDemandeLivraisons());
		}
		c.vue.tableViewLivraisons.refresh();
		c.vue.tableViewDemandesLivraison.refresh();
	}
	protected void changerLivreur(ControleurFenetrePrincipale c, Livreur livreur) {
		if(livreur.getTournee() != null){
			c.vue.afficherLivraisons(livreur, true);
		}else{
			c.vue.afficherDemandesLivraison(livreur, true);
		}
		miseAjourDonneesTableView(c, livreur);
	}

	public void changementLivreur(ControleurFenetrePrincipale c){
		Livreur livreur = c.vue.comboboxLivreur.getValue();
		if(livreur != null){
			this.changerLivreur(c, livreur);
			if(livreur.getTournee() == null){
				if(livreur.getDemandeLivraisons().size() == 0){
					c.changementEtat(c.etatSansDemande);
				}else{
					c.changementEtat(c.etatAvecDemande);
				}
			}else{
				c.changementEtat(c.etatTourneesCalculees);
			}
		}
	}

	public void undo(ListeDeCommandes liste) {
	}
	public void redo(ListeDeCommandes liste) {
	}
}