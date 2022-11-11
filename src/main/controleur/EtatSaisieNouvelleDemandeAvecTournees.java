package controleur;

import javafx.scene.input.MouseEvent;
import modele.*;

public class EtatSaisieNouvelleDemandeAvecTournees extends Etat {
    public EtatSaisieNouvelleDemandeAvecTournees() {
        super.message = "Cliquez sur le plan pour choisir une " +
                "intersection, puis choisissez la plage horaire";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, true);
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
        PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();
        if (!champIdentifiant.isEmpty() && plageHoraire != null) {
            Intersection intersection = c.journee.getPlan().getIntersections()
                    .get(Long.parseLong(champIdentifiant));
            if (c.journee.getPlan().estLivrable(intersection)) {
                DemandeLivraison demande = new DemandeLivraison(intersection, plageHoraire);
                Livreur livreur = c.vue.comboboxLivreur.getValue();
                if(livreur.getNumero() == c.journee.getNbLivreur() && c.journee.dernierLivreurEstSansTourneeCalculee()){

                }
                else{
                    c.vue.comboboxPlageHoraire.setDisable(true);
                    c.vue.tableViewDemandesLivraison.setDisable(false);
                    c.vue.tableViewLivraisons.setDisable(false);

                    c.journee.ajouterDemandeLivraison(demande);
                    c.changementEtat(c.etatSelectionLivraisonPourNouvelleDemande);
                }
                /*Livraison livraison = c.journee.ajouterDemandeLivraisonTournee(demande, c.journee.getTournees().get(livreur - 1).getLivraisons().get(0));
                this.afficherTournee(c, c.journee.getTournees().get(livreur - 1));
                c.vue.afficherLivraisons(true);

                c.vue.buttonAutoriserAjouterLivraison.setDisable(false);
                c.vue.buttonValiderLivraison.setDisable(true);
                c.vue.comboboxPlageHoraire.setDisable(true);
                c.vue.buttonAnnulerLivraison.setDisable(true);

                c.vue.tableViewDemandesLivraison.setDisable(false);
                c.vue.tableViewLivraisons.setDisable(false);
                c.vue.tableViewLivraisons.getItems().add(livraison);
                c.vue.tableViewLivraisons.refresh();

                c.changementEtat(c.etatTourneesCalculees);*/
            }
        }
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        c.changementEtat(c.etatTourneesCalculees);
    }
   

}
