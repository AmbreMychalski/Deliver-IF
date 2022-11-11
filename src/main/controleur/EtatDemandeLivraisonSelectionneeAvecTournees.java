package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EtatDemandeLivraisonSelectionneeAvecTournees extends Etat {
    public EtatDemandeLivraisonSelectionneeAvecTournees() {
        super.message = "Cliquez sur le plan ou appuyez sur " +
                "échap pour quitter la sélection";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c,true);
        c.changementEtat(c.etatTourneesCalculees);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,true);
    }
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        int livreur = c.vue.comboboxLivreur.getValue();
        this.supprimerLivraison(c);
        this.sortieDeSelectionDemande(c,true);
        if(c.journee.getTournees().get(livreur -1) != null){
            this.afficherTournee(c, c.journee.getTournees().get(livreur-1));
            c.changementEtat(c.etatTourneesCalculees);
        } else {
            c.vue.canvasPlanTrajet.getGraphicsContext2D().clearRect(0,0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
            c.changementEtat(c.etatSansDemande);
        }
    }
    public void assignerAutreLivreur(){

    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        switch (ke.getCode()){
            case ESCAPE:
                this.sortieDeSelectionDemande(c,true);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                int livreur = c.vue.comboboxLivreur.getValue();
                this.supprimerLivraison(c);
                this.sortieDeSelectionDemande(c,true);
                this.afficherTournee(c, c.journee.getTournees().get(livreur-1));
        }
    }
}
