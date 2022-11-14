package controleur;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.Livraison;
import modele.Livreur;

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
    public void assignerAutreLivreur(ControleurFenetrePrincipale c){
        Livraison livraison = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
        Livreur ancienLivreur = livraison.getLivreur();
        String livreurText = c.vue.comboboxAssignerLivreur.getValue();
        Livreur livreur;
        if(livreurText.length()<5){ //arbitraire
            int numLivreur = Integer.parseInt(livreurText);
            livreur = c.journee.getLivreurs().get(numLivreur-1); //a changer plus tard
        }else {
            livreur = c.creerLivreur();
        }
        if(livreur != ancienLivreur){
            c.journee.assignerLivraisonNouveauLivreur(livraison, livreur, ancienLivreur);
            miseAjourDonneesTableView(c, ancienLivreur);

            if(livreur.getTournee() != null){
                c.vue.comboboxLivreur.getSelectionModel().select(livreur);
                changerLivreur(c, livreur);
                c.changementEtat(c.etatSelectionLivraisonPourNouvelleDemande);
            }else{
                c.vue.afficherLivraisons(ancienLivreur, true);
                if(ancienLivreur.getDemandeLivraisons().size() > 0){
                    c.changementEtat(c.etatTourneesCalculees);
                }else{
                    c.changementEtat(c.etatSansDemande);
                }
            }
            c.vue.comboboxAssignerLivreur.getSelectionModel().select(null);
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
                break;
            case S:
                c.vue.tableViewLivraisons.getSelectionModel().selectBelowCell();
                this.selectionnerDemande(c, true);
                break;
        }

    }

    public void clicSurComboboxAssignerLivreur(ControleurFenetrePrincipale c){
        String liv = c.vue.comboboxAssignerLivreur.getValue();
        if(liv != null){
            if(liv.length() < 5){
                c.vue.buttonAssignerNvLivreur.setDisable((c.journee.getLivreurs().get(Integer.parseInt(liv)-1) == c.vue.comboboxLivreur.getValue()));
            }else{
                c.vue.buttonAssignerNvLivreur.setDisable(false);
            }
        }
    }
}
