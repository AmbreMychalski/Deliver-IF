package controleur;

import modele.DemandeLivraison;
import modele.Livraison;
import modele.Livreur;
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
    /**********************
     * Changements (a cause de la creation du livreur)  à vérifier avec le boss des livreurs @MathéoJoseph
     * *******************/

        ligne = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);
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

            Livreur livreur = c.vue.comboboxLivreur.getValue();
            DemandeLivraison derniereDemande = livreur.getDemandeLivraisons().get(livreur.getDemandeLivraisons().size()-1);
            Livraison livraisonAAjouter = c.journee.ajouterDemandeLivraisonTournee(derniereDemande, ligne, livreur);

            //this.afficherTournee(c, livreur.getTournee());
            c.vue.afficherLivraisons(livreur, true);


            c.vue.tableViewLivraisons.getItems().add(livraisonAAjouter);
            c.vue.tableViewLivraisons.refresh();

            c.changementEtat(c.etatTourneesCalculees);

            return true;
        }
        return false;

    }
}
