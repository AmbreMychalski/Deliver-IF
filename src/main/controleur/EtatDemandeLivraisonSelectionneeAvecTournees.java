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
    public EtatDemandeLivraisonSelectionneeAvecTournees() {
        super.message = "Cliquez sur la plan ou appuyez sur " +
                "échap pour quitter la sélection";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c);
        c.changementEtat(c.etatTourneesCalculees);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,true);
    }
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        this.supprimerDemandeLivraison(c);
        this.sortieDeSelectionDemande(c);
        miseAJourBoutonEtCanvas(c);
    }
    public void modifierDemande(ControleurFenetrePrincipale c) {
        this.modifierDemandeApresSelection(c);
        c.changementEtat(c.etatModifierDemandeLivraisonAvecTournees);
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        switch (ke.getCode()){
            case ESCAPE:
                this.sortieDeSelectionDemande(c);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                this.supprimerDemandeLivraison(c);
                this.sortieDeSelectionDemande(c);
                miseAJourBoutonEtCanvas(c);
                break;
        }
    }
    private void miseAJourBoutonEtCanvas(ControleurFenetrePrincipale c) {
        if(c.journee.getDemandesLivraison().size() != 0) {
            this.calculerEtAfficherTournee(c);
            c.vue.buttonSauvegarderDemandes.setDisable(false);
            c.changementEtat(c.etatTourneesCalculees);
        } else {
            GraphicsContext gc = c.vue.canvasPlanTrajet.getGraphicsContext2D();
            gc.clearRect(0, 0, c.vue.canvasPlanTrajet.getWidth(),
                    c.vue.canvasPlanTrajet.getHeight());
            c.vue.buttonCalculerTournees.setDisable(true);
            c.vue.buttonSauvegarderDemandes.setDisable(true);
            c.vue.buttonChargerDemandes.setDisable(false);
            c.changementEtat(c.etatSansDemande);
        }
    }
}
