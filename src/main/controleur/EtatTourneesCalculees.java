package controleur;

import javafx.scene.input.MouseEvent;
import modele.Livraison;

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
        this.naviguerSurPlan(c, event);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.buttonValiderLivraison.setDisable(false);
        c.vue.buttonAnnulerLivraison.setDisable(false);
        c.vue.comboboxPlageHoraire.setDisable(false);
        //c.vue.tableViewDemandesLivraison.setDisable(true);
        List<Livraison> listeLivraisons = c.journee.getLivraisonsLivreur(
                c.vue.comboboxLivreur.getValue());
        c.vue.tableViewLivraisons.getItems().addAll(listeLivraisons);
        c.vue.tableViewLivraisons.refresh();
        c.changementEtat(c.etatSaisieNouvelleDemandeAvecTournees);
    }
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        boolean demandeSelectionee = this.selectionnerDemande(c,true);
        if (demandeSelectionee){
            c.changementEtat(c.etatDemandeLivraisonSelectionneeAvecTournees);
        }
    }

    public void clicSurLivreur(ControleurFenetrePrincipale c){
        int livreurSelectionne = c.vue.comboboxLivreur.getValue();
        c.vue.afficherLivraison(true);
        c.vue.tableViewLivraisons.refresh();
    }
}
