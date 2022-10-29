package controleur;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Segment;
import modele.Tournee;
import modele.Trajet;

public class EtatDemandeLivraisonSelectionneeAvecTournees extends Etat {

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c);
        c.etatCourant = c.etatTourneesCalculees;
     
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c);
    }
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        this.supprimerDemandeLivraison(c);
        this.sortieDeSelectionDemande(c);
        
        if(c.journee.getDemandesLivraison().size() != 0) {
            this.calculerEtAfficherTournee(c);
            c.buttonSauvegarderDemandes.setDisable(false);
            c.etatCourant = c.etatTourneesCalculees;
        } else {
            GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
            gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
            c.buttonCalculerTournees.setDisable(true);
            c.buttonSauvegarderDemandes.setDisable(true);
            c.etatCourant = c.etatSansDemande;
        }
    }
    public void modifierDemande(ControleurFenetrePrincipale c) {
        this.modifierDemandeApresSelection(c);
        c.etatCourant = c.etatModifierDemandeLivraisonAvecTournees;
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        if(ke.getCode()== KeyCode.ESCAPE) {
            this.sortieDeSelectionDemande(c);
            c.etatCourant = c.etatTourneesCalculees;
        
        }
    }
}
