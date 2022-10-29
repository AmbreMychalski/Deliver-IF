package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;

public class EtatTourneesCalculees extends Etat{

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c){
        this.sauvegarderListeDemandes(c);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.tableViewDemandesLivraison.setDisable(true);
        c.etatCourant = c.etatSaisieNouvelleDemandeAvecTournees;
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c);
        c.etatCourant = c.etatDemandeLivraisonSelectionneeAvecTournees;
    }
}
