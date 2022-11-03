package controleur;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import modele.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Etat {

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
	
	public void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}

	public  void supprimerLivraison(ControleurFenetrePrincipale c){
		System.out.println("c.etatCourant : "+c.etatCourant);
	}

	private void annulerModif(ControleurFenetrePrincipale c) {
	    c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.buttonValiderLivraison.setDisable(true);
        c.buttonAnnulerLivraison.setDisable(true);
        c.comboboxPlageHoraire.setDisable(true);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
	}

	protected void sortieDeSelectionDemande(ControleurFenetrePrincipale c){
		c.buttonModifierLivraison.setDisable(true);
		c.buttonSupprimerLivraison.setDisable(true);
		c.buttonAutoriserAjouterLivraison.setDisable(false);
		c.afficherDemandeLivraison(true);
		c.textfieldIdentifiantIntersectionSelection.setText("");
		c.textfieldPlageHoraire.setText("");
		c.buttonSauvegarderDemandes.setDisable(false);
	}

	protected  boolean calculerEtAfficherTournee(ControleurFenetrePrincipale c){
		long startTime = System.currentTimeMillis();
		boolean tourneeComplete = c.journee.calculerTournee();
		ControleurFenetrePrincipale.logger.debug("Solution trouvé en :"+ (System.currentTimeMillis() - startTime)+"ms ");
		GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
		gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
		Tournee tournee = c.journee.getTournees().get(c.journee.getTournees().size()-1);
		List<Trajet> trajets = tournee.getTrajets();
		for(Trajet trajet : trajets) {
			List<Segment> segments = trajet.getSegments();
			for(Segment segment : segments) {
				c.dessinerTrajetLatLong(gc, (double)segment.getOrigine().getLatitude(),
						(double)segment.getOrigine().getLongitude(),
						(double)segment.getDestination().getLatitude(),
						(double)segment.getDestination().getLongitude());
			}
		}
		return tourneeComplete;
	}
	protected void sauvegarderListeDemandes(ControleurFenetrePrincipale c){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(".\\data"));
		fileChooser.setTitle("Sauvegarder des demandes de livraison");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
		File fichier = fileChooser.showSaveDialog(c.stage);

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
		c.buttonModifierLivraison.setDisable(true);
		c.textfieldIdentifiantIntersectionSelection.setText("");
		c.buttonSupprimerLivraison.setDisable(true);
		c.buttonAutoriserAjouterLivraison.setDisable(true);
		c.buttonValiderLivraison.setDisable(false);
		c.buttonAnnulerLivraison.setDisable(false);
		c.comboboxPlageHoraire.setDisable(false);
		c.tableViewDemandesLivraison.setDisable(true);
	}

	protected void effectuerModification(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		String champIdentifiant = c.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.comboboxPlageHoraire.getValue();

		Intersection intersection;
		if(champIdentifiant.isEmpty()) {
			intersection = null;
		} else {
			intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
		}


		DemandeLivraison demande = c.journee.getDemandesLivraison().get(c.journee.getDemandesLivraison().indexOf(ligne));
		demande.modifierDemandeLivraison(intersection, plageHoraire);
		c.tableViewDemandesLivraison.refresh();
		c.afficherDemandeLivraison(true);
		c.dessinerIntersectionLatLong(c.canvasInterieurPlan.getGraphicsContext2D(),
				demande.getIntersection().getLatitude(),
				demande.getIntersection().getLongitude(),
				c.COULEUR_POINT_LIVRAISON_SELECTIONNE,
				c.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
				true,
				"Rectangle");
		c.textfieldIdentifiantIntersectionSelection.setText(demande.getIdIntersection().toString());
		c.textfieldPlageHoraire.setText(demande.getPlageHoraire().toString());


		c.buttonModifierLivraison.setDisable(false);
		c.buttonSupprimerLivraison.setDisable(false);
		c.buttonValiderLivraison.setDisable(true);
		c.buttonAnnulerLivraison.setDisable(true);
		c.comboboxPlageHoraire.setDisable(true);
		c.comboboxPlageHoraire.setValue(null);
		c.tableViewDemandesLivraison.setDisable(false);
		c.textfieldIdentifiantIntersection.setText("");
	}

	protected void annulerModification(ControleurFenetrePrincipale c){
		c.buttonModifierLivraison.setDisable(false);
		c.buttonSupprimerLivraison.setDisable(false);
		c.buttonValiderLivraison.setDisable(true);
		c.buttonAnnulerLivraison.setDisable(true);
		c.comboboxPlageHoraire.setDisable(true);
		c.tableViewDemandesLivraison.setDisable(false);
	}

	protected boolean selectionnerDemande(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		if (ligne != null) {
			c.afficherDemandeLivraison(true);
			c.dessinerIntersectionLatLong(c.canvasInterieurPlan.getGraphicsContext2D(),
					ligne.getIntersection().getLatitude(),
					ligne.getIntersection().getLongitude(),
					c.COULEUR_POINT_LIVRAISON_SELECTIONNE,
					c.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
					true,
					"Rectangle");

			c.titlePaneSelectionDemande.setVisible(true);
			c.textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
			c.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
			c.buttonCalculerTournees.setDisable(true);
			c.buttonAutoriserAjouterLivraison.setDisable(true);
			c.buttonSauvegarderDemandes.setDisable(true);
			c.buttonChargerDemandes.setDisable(true);
			c.buttonModifierLivraison.setDisable(false);
			c.buttonSupprimerLivraison.setDisable(false);
			return true;
		}
		return false;

	}
	protected void validerAjoutDemande(ControleurFenetrePrincipale c){
		String champIdentifiant = c.textfieldIdentifiantIntersection.getText();
		PlageHoraire plageHoraire = c.comboboxPlageHoraire.getValue();
		if(!champIdentifiant.isEmpty() && plageHoraire != null) {
			Intersection intersection = c.journee.getPlan().getIntersections()
					.get(Long.parseLong(champIdentifiant));
			if(c.journee.getPlan().estLivrable(intersection)){
				DemandeLivraison demande =
						new DemandeLivraison(intersection, plageHoraire);
				c.journee.ajouterDemandeLivraison(demande);
				c.tableViewDemandesLivraison.getItems().add(demande);
				c.tableViewDemandesLivraison.refresh();
				c.afficherDemandeLivraison(true);
				c.buttonValiderLivraison.setDisable(true);
				c.buttonAnnulerLivraison.setDisable(true);
				c.comboboxPlageHoraire.setDisable(true);
				c.comboboxPlageHoraire.setValue(null);
				c.textfieldIdentifiantIntersection.setText("");
				c.tableViewDemandesLivraison.setDisable(false);
				c.buttonSauvegarderDemandes.setDisable(false);
			}
			else{
				ControleurFenetrePrincipale.logger.warn("L'intersection n'est pas livrable");
			}
		} else {
			ControleurFenetrePrincipale.logger.warn("Informations manquantes pour l'ajout de la demande");
		}
	}

	protected  void naviguerSurPlan(ControleurFenetrePrincipale c, MouseEvent event){
		if (c.planCharge != null) {
			ControleurFenetrePrincipale.logger.debug("Clic sur le canvas, (x,y)=("
					+ event.getX() + "," + event.getY() + ") (lat,long)="
					+ c.convertirYEnLatitude(event.getY()) + ","
					+ c.convertirXEnLongitude(event.getX()));
			Intersection intersectionTrouvee =
					c.trouverIntersectionCoordoneesPixels(event.getX(),
							event.getY());
			//ControleurFenetrePrincipale.logger.debug("Intersection trouvée = " + intersectionTrouvee);
			if (intersectionTrouvee != null) {
				c.textfieldIdentifiantIntersection.setText(
						intersectionTrouvee.getIdIntersection().toString());


				GraphicsContext gc = c.canvasInterieurPlan.getGraphicsContext2D();
				gc.clearRect(0, 0, c.canvasInterieurPlan.getWidth(), c.canvasInterieurPlan.getHeight());
				c.dessinerIntersectionLatLong(gc,
						intersectionTrouvee.getLatitude(),
						intersectionTrouvee.getLongitude(),
						Color.DARKORCHID,
						c.TAILLE_CERCLE_INTERSECTION_SELECTIONNEE,
						true,
						"Cercle");

				c.afficherDemandeLivraison(false);

			} else {
				c.textfieldIdentifiantIntersection.setText("");
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
		File fichier = fileChooser.showOpenDialog(c.stage);
		System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

		ArrayList<DemandeLivraison> listeDemandes = c.journee.chargerDemandesLivraison(fichier);
		c.tableViewDemandesLivraison.getItems().addAll(listeDemandes);
		c.tableViewDemandesLivraison.refresh();
		c.afficherDemandeLivraison(true);
	}

	protected void supprimerDemandeLivraison(ControleurFenetrePrincipale c){
		DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
		if(ligne != null) {
			c.journee.supprimerDemandeLivraison(ligne);
			c.tableViewDemandesLivraison.getItems().remove(ligne);
			c.tableViewDemandesLivraison.refresh();
			c.textfieldIdentifiantIntersectionSelection.setText("");
			c.textfieldPlageHoraire.setText("");
			c.afficherDemandeLivraison(true);
		}
	}
	protected  void annulerAjout (ControleurFenetrePrincipale c){
		c.buttonValiderLivraison.setDisable(true);
		c.buttonAnnulerLivraison.setDisable(true);
		c.comboboxPlageHoraire.setDisable(true);
		c.tableViewDemandesLivraison.setDisable(false);
		c.textfieldIdentifiantIntersection.setText("");
		c.comboboxPlageHoraire.setValue(null);

	}

}
