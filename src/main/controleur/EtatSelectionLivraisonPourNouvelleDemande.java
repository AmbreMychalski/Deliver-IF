package controleur;

import modele.DemandeLivraison;
import modele.Intersection;
import modele.Livraison;
import modele.Livreur;
import vue.FenetrePlusieursLivraisonsAuMemeEndroit;
import vue.VueFenetrePrincipale;

import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class EtatSelectionLivraisonPourNouvelleDemande extends Etat{
    public EtatSelectionLivraisonPourNouvelleDemande() {
        super.message = "Sélectionner la livraison qui précédera" +
                " la nouvelle demande, ou l'entrepot si vous"
                + " souhaitez la mettre en première position";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        Intersection intersectionTrouvee = this.naviguerSurPlan(c, event, true);
            if (c.getJournee().getPlan().getEntrepot().equals(intersectionTrouvee)) {
                selectionPourNouvelleDemande(c, null);
            }
            if(intersectionTrouvee != null) {
                ArrayList<Livraison> livraisonsAssociees = new ArrayList<>();

                for (Livraison livraison : c.vue.comboboxLivreur.getValue().getLivraisons()) {
                    if (livraison.getDemandeLivraison().getIntersection().equals(intersectionTrouvee)) {
                        livraisonsAssociees.add(livraison);
                    }
                }

                if (livraisonsAssociees.size() == 1) {
                    selectionPourNouvelleDemande(c, livraisonsAssociees.get(0));
                } else if (livraisonsAssociees.size() > 1) {
                    FenetrePlusieursLivraisonsAuMemeEndroit.display(c, null, livraisonsAssociees, true);
                }
            }
    }

    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c,false);
    }

    public boolean selectionnerDemande(ControleurFenetrePrincipale c, boolean livraison) {
        Livraison ligne;

        ligne = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();

        selectionPourNouvelleDemande(c, ligne);
        return true;
    }
    public void selectionPourNouvelleDemande(ControleurFenetrePrincipale c,
                                             Livraison ligne) {
        c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(), true);

        if(ligne != null) {
            c.vue.dessinerIntersection(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                    ligne.getDemandeLivraison().getIntersection(),
                    c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                    c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                    true,
                    VueFenetrePrincipale.FormeIntersection.RECTANGLE
            );

            c.vue.titlePaneSelectionDemande.setVisible(true);
            c.vue.textfieldIdentifiantIntersection.setText(ligne.getDemandeLivraison().getIdIntersection().toString());
            remplirLabelRuesIntersection(c, ligne.getDemandeLivraison().getIntersection());
            c.vue.textfieldPlageHoraire.setText(ligne.getDemandeLivraison().getPlageHoraire().toString());
        }

        Livreur livreur = c.vue.comboboxLivreur.getValue();
        DemandeLivraison derniereDemande = livreur.getDemandeLivraisons()
                .get(livreur.getDemandeLivraisons().size()-1);
        Commande commandeAjout = new CommandeAjouter(c, livreur, ligne, derniereDemande);

        c.getListeCommandes().ajouterCommande(commandeAjout);
        c.changementEtat(c.etatTourneesCalculees);
    }
}
