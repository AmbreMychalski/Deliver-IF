package controleur;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import lombok.Getter;
import modele.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Etat {
	protected String message;
	public Etat() {
		this.message = "Etat";
	}

	public void chargerPlan(ControleurFenetrePrincipale c) throws FichierNonConformeException {}
	
	public void ajouterDemande(ControleurFenetrePrincipale c){}
	
	public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event){}
	
	public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}

	public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
	
	public void chargerListeDemandes(ControleurFenetrePrincipale c) {}
	
	public void supprimerDemande(ControleurFenetrePrincipale c) {}
	
	public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
	
	public void calculerTournees(ControleurFenetrePrincipale c) {}
	
	public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
	
	public void fermerFenetre(ControleurFenetrePrincipale c) {}
	
	public void quitterLogiciel(ControleurFenetrePrincipale c) {}
	
	public void modifierDemande(ControleurFenetrePrincipale c) {}

	public void ajouterLivreur(ControleurFenetrePrincipale c) {}

	public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}

	public  void supprimerLivraison(ControleurFenetrePrincipale c){
		System.out.println("c.etatCourant : "+c.etatCourant);
	}

	private void annulerModif(ControleurFenetrePrincipale c) {
	    c.vue.buttonAutoriserAjouterLivraison.setDisable(false);
        c.vue.buttonValiderLivraison.setDisable(true);
        c.vue.buttonAnnulerLivraison.setDisable(true);
        c.vue.comboboxPlageHoraire.setDisable(true);
        c.changementEtat(c.etatDemandeLivraisonSelectionneeSansTournees);
	}

	protected void sortieDeSelectionDemande(ControleurFenetrePrincipale c){
		c.vue.buttonModifierLivraison.setDisable(true);
		c.vue.buttonSupprimerLivraison.setDisable(true);
		c.vue.buttonAutoriserAjouterLivraison.setDisable(false);
		c.vue.afficherDemandeLivraison(true);
		c.vue.textfieldIdentifiantIntersectionSelection.setText("");
		c.vue.textfieldPlageHoraire.setText("");
		c.vue.buttonSauvegarderDemandes.setDisable(false);
	}

	protected  boolean calculerEtAfficherTournee(ControleurFenetrePrincipale c){
		int livreur = c.vue.comboboxLivreur.getValue();
		long startTime = System.currentTimeMillis();
		boolean tourneeComplete = c.journee.calculerTournee();
		ControleurFenetrePrincipale.logger.debug("tourneeComplete = " + tourneeComplete);
		ControleurFenetrePrincipale.logger.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");
		GraphicsContext gc = c.vue.canvasPlanTrajet.getGraphicsContext2D();
		gc.clearRect(0, 0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
		Tournee tournee = c.journee.getTournees().get(livreur-1);
		List<Trajet> trajets = tournee.getTrajets();
		for(Trajet trajet : trajets) {
			List<Segment> segments = trajet.getSegments();
			for(Segment segment : segments) {
				c.vue.dessinerTrajetLatLong(gc, segment.getOrigine().getLatitude(),
						segment.getOrigine().getLongitude(),
						segment.getDestination().getLatitude(),
						segment.getDestination().getLongitude());
			}
		}
		List<Livraison> listeLivraisons = c.journee.getLivraisonsLivreur(livreur);
		c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
		c.vue.tableViewLivraisons.refresh();

		return tourneeComplete;
	}
	protected void sauvegarderListeDemandes(ControleurFenetrePrincipale c){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.setTitle("Sauvegarder des demandes de livraison");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		File fichier = fileChooser.showSaveDialog(c.vue.getStage());

		if (fichier != null) {
			ControleurFenetrePrincipale.logger.info("Sauvegarde à l'emplacement "
					+ fichier.getAbsolutePath());
			c.journee.sauvegarderDemandesLivraison(fichier);
		} else {
			ControleurFenetrePrincipale.logger
					.error("Erreur lors de la sauvegarde des demandes");
		}
	}

	protected void modifierDemandeApresSelection(ControleurFenetrePrincipale c){
		c.vue.buttonModifierLivraison.setDisable(true);
		c.vue.textfieldIdentifiantIntersectionSelection.setText("");
		c.vue.buttonSupprimerLivraison.setDisable(true);
		c.vue.buttonAutoriserAjouterLivraison.setDisable(true);
		c.vue.buttonValiderLivraison.setDisable(false);
		c.vue.buttonAnnulerLivraison.setDisable(false);
		c.vue.comboboxPlageHoraire.setDisable(false);
		c.vue.tableViewDemandesLivraison.setDisable(true);
	}

	protected void effectuerModification(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();

		Intersection intersection;
		if(champIdentifiant.isEmpty()) {
			intersection = null;
		} else {
			intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
		}


		DemandeLivraison demande = c.journee.getDemandesLivraison().get(c.journee.getDemandesLivraison().indexOf(ligne));
		c.journee.modifierDemandeLivraison(demande, intersection, plageHoraire);

		//c.vue.tableViewDemandesLivraison.refresh();
		//c.vue.afficherDemandeLivraison(true);
		c.vue.dessinerIntersection(c.vue.canvasInterieurPlan.getGraphicsContext2D(),
				demande.getIntersection(),
				c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
				c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
				true,
				"Rectangle");
		c.vue.textfieldIdentifiantIntersectionSelection.setText(demande.getIdIntersection().toString());
		c.vue.textfieldPlageHoraire.setText(demande.getPlageHoraire().toString());


		c.vue.buttonModifierLivraison.setDisable(false);
		c.vue.buttonSupprimerLivraison.setDisable(false);
		c.vue.buttonValiderLivraison.setDisable(true);
		c.vue.buttonAnnulerLivraison.setDisable(true);
		c.vue.comboboxPlageHoraire.setDisable(true);
		c.vue.comboboxPlageHoraire.setValue(null);
		c.vue.tableViewDemandesLivraison.setDisable(false);
		c.vue.textfieldIdentifiantIntersection.setText("");
	}

	protected void annulerModification(ControleurFenetrePrincipale c){
		c.vue.buttonModifierLivraison.setDisable(false);
		c.vue.buttonSupprimerLivraison.setDisable(false);
		c.vue.buttonValiderLivraison.setDisable(true);
		c.vue.buttonAnnulerLivraison.setDisable(true);
		c.vue.comboboxPlageHoraire.setDisable(true);
		c.vue.tableViewDemandesLivraison.setDisable(false);
	}

	protected boolean selectionnerDemande(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		if (ligne != null) {
			c.vue.afficherDemandeLivraison(true);
			c.vue.dessinerIntersection(c.vue.canvasInterieurPlan.getGraphicsContext2D(),
					ligne.getIntersection(),
					c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
					c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
					true,
					"Rectangle");

			c.vue.titlePaneSelectionDemande.setVisible(true);
			c.vue.textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
			c.vue.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
			c.vue.buttonCalculerTournees.setDisable(true);
			c.vue.buttonAutoriserAjouterLivraison.setDisable(true);
			c.vue.buttonSauvegarderDemandes.setDisable(true);
			c.vue.buttonChargerDemandes.setDisable(true);
			c.vue.buttonModifierLivraison.setDisable(false);
			c.vue.buttonSupprimerLivraison.setDisable(false);
			return true;
		}
		return false;

	}
	protected boolean validerAjoutDemande(ControleurFenetrePrincipale c){
		String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();
		if(!champIdentifiant.isEmpty() && plageHoraire != null) {
			Intersection intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
			if(c.journee.getPlan().estLivrable(intersection)){
				DemandeLivraison demande =
						new DemandeLivraison(intersection, plageHoraire);
				c.vue.tableViewDemandesLivraison.getItems().add(demande);
				c.journee.ajouterDemandeLivraison(demande);
				//c.vue.tableViewDemandesLivraison.refresh();
				//c.vue.afficherDemandeLivraison(true);
				c.vue.buttonValiderLivraison.setDisable(true);
				c.vue.buttonAnnulerLivraison.setDisable(true);
				c.vue.comboboxPlageHoraire.setDisable(true);
				c.vue.comboboxPlageHoraire.setValue(null);
				c.vue.textfieldIdentifiantIntersection.setText("");
				c.vue.tableViewDemandesLivraison.setDisable(false);
				c.vue.buttonSauvegarderDemandes.setDisable(false);
				return true;
			}
			else{
				ControleurFenetrePrincipale.logger.warn("L'intersection n'est pas livrable");
			}
		} else {
			ControleurFenetrePrincipale.logger.warn("Informations manquantes pour l'ajout de la demande");
		}
		return false;
	}

	protected  void naviguerSurPlan(ControleurFenetrePrincipale c, MouseEvent event){
		if (c.journee.getPlan() != null) {
			/*ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=("
					+ event.getX() + "," + event.getY() + ") (lat,long)="
					+ c.vue.convertirYEnLatitude(event.getY()) + ","
					+ c.vue.convertirXEnLongitude(event.getX()));*/
			Intersection intersectionTrouvee =
					c.vue.trouverIntersectionCoordoneesPixels(event.getX(),
							event.getY());
			//ControleurFenetrePrincipale.logger.debug("Intersection trouvée = " + intersectionTrouvee);
			if (intersectionTrouvee != null) {
				c.vue.textfieldIdentifiantIntersection.setText(
						intersectionTrouvee.getIdIntersection().toString());

				GraphicsContext gc = c.vue.canvasInterieurPlan.getGraphicsContext2D();
				gc.clearRect(0, 0, c.vue.canvasInterieurPlan.getWidth(), c.vue.canvasInterieurPlan.getHeight());
				c.vue.dessinerIntersection(gc,
						intersectionTrouvee,
						Color.DARKORCHID,
						c.vue.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
						true,
						"Cercle");

				c.vue.afficherDemandeLivraison(false);

			} else {
				c.vue.textfieldIdentifiantIntersection.setText("");
			}
		} else {
			ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=("
					+ event.getX() + "," + event.getY() + ")");
		}

	}

	protected void chargerDemandes(ControleurFenetrePrincipale c){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		fileChooser.setTitle("Charger des demandes de livraison");
		File fichier = fileChooser.showOpenDialog(c.vue.getStage());
		System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

		ArrayList<DemandeLivraison> listeDemandes = c.journee.chargerDemandesLivraison(fichier);
		c.vue.tableViewDemandesLivraison.getItems().addAll(listeDemandes);
		c.vue.tableViewDemandesLivraison.refresh();
		//c.vue.afficherDemandeLivraison(true);
	}

	protected void supprimerDemandeLivraison(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		if(ligne != null) {
			c.vue.tableViewDemandesLivraison.getItems().remove(ligne);
			c.journee.supprimerDemandeLivraison(ligne);
			c.vue.tableViewDemandesLivraison.refresh();
			c.vue.textfieldIdentifiantIntersectionSelection.setText("");
			c.vue.textfieldPlageHoraire.setText("");
			//c.vue.afficherDemandeLivraison(true);
		}
	}
	protected  void annulerAjout (ControleurFenetrePrincipale c){
		c.vue.buttonValiderLivraison.setDisable(true);
		c.vue.buttonAnnulerLivraison.setDisable(true);
		c.vue.comboboxPlageHoraire.setDisable(true);
		c.vue.tableViewDemandesLivraison.setDisable(false);
		c.vue.textfieldIdentifiantIntersection.setText("");
		c.vue.comboboxPlageHoraire.setValue(null);

	}

}
