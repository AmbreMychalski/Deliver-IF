package controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import modele.Livraison;
import modele.Livreur;
import modele.Tournee;
import vue.VueFenetrePrincipale;

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
        this.selectionTrajet(c);
    }

    public void supprimerDemande(ControleurFenetrePrincipale c) {
        supprimerLivraison(c);
    }

    public void assignerAutreLivreur(ControleurFenetrePrincipale c) {
        Livreur livreur;

        Livraison livraison = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
        Livreur ancienLivreur = livraison.getLivreur();
        String livreurText = c.vue.comboboxAssignerLivreur.getValue();

        if(livreurText.length() < 5) { //arbitraire
            int numLivreur = Integer.parseInt(livreurText);
            livreur = c.journee.getLivreurs().get(numLivreur-1); //a changer plus tard
        } else {
            livreur = c.creerLivreur();
        }

        if(livreur != ancienLivreur) {
            c.journee.assignerLivraisonNouveauLivreur(livraison, livreur, ancienLivreur);
            miseAjourDonneesTableView(c, ancienLivreur);

            if(livreur.getTournee() != null) {
                c.vue.comboboxLivreur.getSelectionModel().select(livreur);
                changerLivreur(c, livreur);
                c.changementEtat(c.etatSelectionLivraisonPourNouvelleDemande);
            } else {
                c.vue.afficherLivraisons(ancienLivreur, true);

                if(ancienLivreur.getDemandeLivraisons().size() > 0) {
                    c.changementEtat(c.etatTourneesCalculees);
                } else {
                    c.changementEtat(c.etatSansDemande);
                }
            }

            c.vue.comboboxAssignerLivreur.getSelectionModel().select(null);
            c.viderListeDeCommandes();
        }
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        super.touchePressee(c, ke);

        switch (ke.getCode()) {
            case ESCAPE:
                this.sortieDeSelectionDemande(c, true);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                supprimerLivraison(c);
                break;
            case Z:
                c.vue.tableViewLivraisons.getSelectionModel().selectAboveCell();
                this.selectionnerDemande(c, true);
                this.selectionTrajet(c);
                break;
            case S:
                c.vue.tableViewLivraisons.getSelectionModel().selectBelowCell();
                this.selectionnerDemande(c, true);
                this.selectionTrajet(c);
                break;
        }
    }

    public void clicSurComboboxAssignerLivreur(ControleurFenetrePrincipale c) {
        String liv = c.vue.comboboxAssignerLivreur.getValue();

        if(liv != null) {
            if(liv.length() < 5) {
                c.vue.buttonAssignerNvLivreur.setDisable(
                        (c.journee.getLivreurs().get(Integer.parseInt(liv)-1)
                                == c.vue.comboboxLivreur.getValue())
                );
            } else {
                c.vue.buttonAssignerNvLivreur.setDisable(false);
            }
        }
    }
    public void zoomScroll(ControleurFenetrePrincipale c, ScrollEvent event) {
        double deltaY = event.getDeltaY();
        if(deltaY>0){
            c.vue.redessinerPlan(true,1.5);
        }
        else{
            c.vue.redessinerPlan(true,0.6667);
        }

        if(c.vue.comboboxLivreur.getValue().getTournee() != null) {
            c.vue.afficherLivraisons(c.vue.comboboxLivreur.getValue(),
                    true);
        } else {
            c.vue.afficherDemandesLivraison(c.vue.comboboxLivreur.getValue(),
                    true);
        }

        Livraison liv = c.vue.tableViewLivraisons.getSelectionModel()
                .getSelectedItem();

        Livreur livreur = c.vue.comboboxLivreur.getValue();
        Tournee tournee = livreur.getTournee();
        int indexLivr = (tournee.getLivraisons()).indexOf(liv);

        c.vue.dessinerSurbrillanceTrajet(tournee.getTrajets().get(indexLivr),
                c.vue.canvasPlanTrajet.getGraphicsContext2D());

        c.vue.dessinerIntersection(
                c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(),
                liv.getDemandeLivraison().getIntersection(),
                c.vue.COULEUR_POINT_LIVRAISON_SELECTIONNE,
                c.vue.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE,
                true,
                VueFenetrePrincipale.FormeIntersection.RECTANGLE
        );
    }
}
