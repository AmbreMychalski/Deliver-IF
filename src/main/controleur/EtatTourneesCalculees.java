package controleur;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;

import java.util.List;

public class EtatTourneesCalculees extends Etat{
    public EtatTourneesCalculees() {
        super.message = "EtatTourneesCalculees";
    }

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c){
        this.sauvegarderListeDemandes(c);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.buttonValiderLivraison.setDisable(false);
        c.vue.buttonAnnulerLivraison.setDisable(false);
        c.vue.comboboxPlageHoraire.setDisable(false);
        //c.vue.tableViewDemandesLivraison.setDisable(true);
        //List<Livraison> listeLivraisons = c.journee.getLivraisonsLivreur(c.vue.comboboxLivreur.getValue());
       // c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
        //c.vue.tableViewLivraisons.refresh();
        c.changementEtat(c.etatSaisieNouvelleDemandeAvecTournees);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c);
        if (demandeSelectionee){
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }
}
