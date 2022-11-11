package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Livraison;
import vue.VueFenetrePrincipale;

public class EtatSelectionLivraisonPourNouvelleDemande extends Etat{
    public EtatSelectionLivraisonPourNouvelleDemande() {
        super.message = "Sélectionner la livraison qui viendra avant la livraison correspondant à la nouvelle deamnde";
    }

    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,false);
    }
    protected boolean selectionnerDemande(ControleurFenetrePrincipale c, boolean livraison){
        Livraison ligne;

        ligne = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();

        if (ligne != null) {

            c.vue.afficherLivraison(true);
            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    ligne.getDemandeLivraison().getIntersection(),
                    c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.RECTANGLE);


            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.vue.textfieldIdentifiantIntersection.setText(ligne.getDemandeLivraison().getIdIntersection().toString());
            remplirLabelRuesIntersection(c, ligne.getDemandeLivraison().getIntersection());
            c.vue.textfieldPlageHoraire.setText(ligne.getDemandeLivraison().getPlageHoraire().toString());

            int livreur = c.vue.comboboxLivreurNouvelleDemande.getValue();
            DemandeLivraison derniereDemande = c.journee.getDemandesLivraison().get(c.journee.getDemandesLivraison().size()-1);
            Livraison livraisonAAjoutee = c.journee.ajouterDemandeLivraisonTournee(derniereDemande, ligne);
            this.afficherTournee(c, c.journee.getTournees().get(livreur - 1));
            c.vue.afficherLivraison(true);

            c.vue.buttonAutoriserAjouterLivraison.setDisable(false);
            c.vue.buttonValiderLivraison.setDisable(true);
            c.vue.comboboxPlageHoraire.setDisable(true);
            c.vue.buttonAnnulerLivraison.setDisable(true);


            c.vue.tableViewLivraisons.getItems().add(livraisonAAjoutee);
            c.vue.tableViewLivraisons.refresh();

            c.changementEtat(c.etatTourneesCalculees);

            return true;
        }
        return false;

    }
}
