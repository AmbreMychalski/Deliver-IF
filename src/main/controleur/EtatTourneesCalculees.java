package controleur;

import javafx.scene.input.MouseEvent;
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
        this.naviguerSurPlan(c, event, true);
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
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        if(livreur != null){
            this.changerLivreur(c, livreur);
            if(livreur.getTournee() == null){
                c.changementEtat(c.etatAvecDemande);
                c.vue.buttonChargerDemandes.setDisable(true);
            }else{
                c.changementEtat(c.etatTourneesCalculees);
            }
        }
    }
}
