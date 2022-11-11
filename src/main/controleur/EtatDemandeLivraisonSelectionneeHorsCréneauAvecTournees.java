package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EtatDemandeLivraisonSelectionneeHorsCréneauAvecTournees extends Etat {
    public EtatDemandeLivraisonSelectionneeHorsCréneauAvecTournees() {
        super.message = "Certaines livraisons seront effectuées hors des " +
                "horaires données : veuillez ajouter un nouveau livreur " +
                "pour régler ce problème";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c,true);
        c.changementEtat(c.etatTourneesCalculees);
     
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,true);
    }
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        this.supprimerLivraison(c);
        this.sortieDeSelectionDemande(c,true);
        miseAJourBoutonEtCanvas(c);
    }
    public void modifierDemande(ControleurFenetrePrincipale c) {
        this.modifierDemandeApresSelection(c);
        c.changementEtat(c.etatModifierDemandeLivraisonAvecTournees);
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        switch (ke.getCode()){
            case ESCAPE:
                this.sortieDeSelectionDemande(c,true);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                this.supprimerLivraison(c);
                this.sortieDeSelectionDemande(c,true);
                miseAJourBoutonEtCanvas(c);
                break;
        }
    }
    private void miseAJourBoutonEtCanvas(ControleurFenetrePrincipale c) {
        if(c.journee.getDemandesLivraison().size() != 0) {
            this.calculerEtAfficherTournee(c);
            c.changementEtat(c.etatTourneesCalculees);
        } else {
            GraphicsContext gc = c.vue.canvasPlanTrajet.getGraphicsContext2D();
            gc.clearRect(0, 0, c.vue.canvasPlanTrajet.getWidth(),
                    c.vue.canvasPlanTrajet.getHeight());
            c.changementEtat(c.etatSansDemande);
        }
    }
}
