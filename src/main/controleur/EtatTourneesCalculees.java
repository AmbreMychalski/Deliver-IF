package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;
import modele.Livreur;

import java.util.List;

public class EtatTourneesCalculees extends Etat{
    public EtatTourneesCalculees() {
        super.message = "Ajoutez des demandes, visualisez les " +
                "feuilles de route ou modifiez les tournées";
    }

    public  void sauvegarderDemandes(ControleurFenetrePrincipale c){
        this.sauvegarderListeDemandes(c);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, true);
        Livraison livraisonAssociee = null;
        for(Livraison livraison : c.vue.comboboxLivreur.getValue().getLivraisons()){
            if(intersectionTrouvee == livraison.getDemandeLivraison().getIntersection()){
                livraisonAssociee = livraison;
            }
        }
        if (livraisonAssociee != null) {
            c.vue.tableViewLivraisons.getSelectionModel().select(livraisonAssociee);
            this.selectionnerDemande(c, true);
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.tableViewDemandesLivraison.setDisable(true);
        c.vue.tableViewLivraisons.setDisable(true);
        c.changementEtat(c.etatSaisieNouvelleDemandeAvecTournees);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c,true);
        if (demandeSelectionee){
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }
}
