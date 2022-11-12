package controleur;

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

            this.majComboboxLivreur(c);
        }
        if(livreur != ancienLivreur){
            if(livreur.getTournee()==null) {
                c.journee.assignerLivraisonNouveauLivreur(livraison, livreur, ancienLivreur);
                this.miseAjourDonneesTableView(c, ancienLivreur);
                c.vue.afficherLivraisons(ancienLivreur, true);
                c.changementEtat(c.etatTourneesCalculees);
            }
            else{
                this.changerLivreur(c, livreur);
                livreur.ajouterDemandeLivraison(livraison.getDemandeLivraison());
                c.journee.supprimerLivraisonJournee(livreur, livraison);
                c.vue.comboboxLivreur.getSelectionModel().select(livreur.getNumero()-1);
                this.afficherTournee(c, livreur.getTournee());
                c.vue.afficherLivraisons(livreur, true);
                c.changementEtat(c.etatSelectionLivraisonPourNouvelleDemande);
            }
            //c.journee.calculerTourneeNouveauLivreur(livreur);
            //this.afficherTournee(c, livreur.getTournee());
        }
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        switch (ke.getCode()){
            case ESCAPE:
                this.sortieDeSelectionDemande(c,true);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                supprimerLivraison(c);
        }
    }

    public void clicSurComboboxAssignerLivreur(ControleurFenetrePrincipale c){
        String liv = c.vue.comboboxAssignerLivreur.getValue();
        if(liv != null){
            if(liv.length() < 5){
                c.vue.buttonAssignerNvLivreur.setDisable(c.journee.getLivreurs().get(Integer.parseInt(liv) - 1) == c.vue.comboboxLivreur.getValue());
            }else{
                c.vue.buttonAssignerNvLivreur.setDisable(false);
            }
        }
    }
}
