package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Livreur;

public class EtatDemandeLivraisonSelectionneeSansTournees extends Etat {
    public EtatDemandeLivraisonSelectionneeSansTournees() {
        super.message = "Cliquez sur le plan ou appuyez sur échap " +
                "pour quitter la sélection";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c,false);
        c.changementEtat(c.etatAvecDemande);
    }

    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,false);
    }

    public void supprimerDemande(ControleurFenetrePrincipale c) {
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        this.supprimerDemandeLivraison(c, livreur);
        this.sortieDeSelectionDemande(c,false);

        if(livreur.getDemandeLivraisons().size() > 0) {
            c.changementEtat(c.etatAvecDemande);
        } else {
            c.changementEtat(c.etatSansDemande);
        }
    }

    public void modifierDemande(ControleurFenetrePrincipale c) {
        DemandeLivraison ligne = c.vue.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();

        c.vue.textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
        c.vue.textfieldIdentifiantIntersection.setText(ligne.getIdIntersection().toString());
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.comboboxPlageHoraire.getSelectionModel().select(ligne.getPlageHoraire());
        this.remplirLabelRuesIntersection(c, ligne.getIntersection());
        c.vue.tableViewDemandesLivraison.setDisable(true);
        c.changementEtat(c.etatModifierDemandeLivraisonSansTournees);
    }

    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        super.touchePressee(c, ke);

        switch (ke.getCode()) {
            case ESCAPE:
                this.sortieDeSelectionDemande(c,false);
                c.changementEtat(c.etatAvecDemande);
                break;
            case DELETE:
                Livreur livreur = c.vue.comboboxLivreur.getValue();
                this.supprimerDemandeLivraison(c, livreur);
                this.sortieDeSelectionDemande(c,false);

                if(livreur.getDemandeLivraisons().size() > 0){
                    c.changementEtat(c.etatAvecDemande);
                } else {
                    c.changementEtat(c.etatSansDemande);
                }
                
                break;
            case Z:
                c.vue.tableViewDemandesLivraison.getSelectionModel().selectAboveCell();
                this.selectionnerDemande(c, false);
                break;
            case S:
                c.vue.tableViewDemandesLivraison.getSelectionModel().selectBelowCell();
                this.selectionnerDemande(c, false);
                break;
        }
    }
}
