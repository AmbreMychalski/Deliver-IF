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
        Livreur livreur = c.vue.comboboxLivreur.getValue();
        this.supprimerLivraison(c);
        this.sortieDeSelectionDemande(c,true);
        if(livreur.getTournee() != null){
            this.afficherTournee(c, livreur.getTournee());
            c.changementEtat(c.etatTourneesCalculees);
        } else {
            c.vue.canvasPlanTrajet.getGraphicsContext2D().clearRect(0,0, c.vue.canvasPlanTrajet.getWidth(), c.vue.canvasPlanTrajet.getHeight());
            c.changementEtat(c.etatSansDemande);
        }
    }
    public void assignerAutreLivreur(ControleurFenetrePrincipale c){
        Livraison livraison = c.vue.tableViewLivraisons.getSelectionModel().getSelectedItem();
        String livreurText = c.vue.comboboxAssignerLivreur.getValue();
        Livreur livreur;
        if(livreurText.length()<5){ //arbitraire
            int numLivreur = Integer.parseInt(livreurText);
            livreur = c.journee.getLivreurs().get(numLivreur-1); //a changer plus tard
        }else {
            livreur = new Livreur();
            c.journee.ajouterLivreur(livreur);
            this.majComboboxLivreur(c);
        }
        c.journee.assignerLivraisonNouveauLivreur(livraison, livreur);
        //c.journee.calculerTourneeNouveauLivreur(livreur);
        //this.afficherTournee(c, livreur.getTournee());
    }
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        switch (ke.getCode()){
            case ESCAPE:
                this.sortieDeSelectionDemande(c,true);
                c.changementEtat(c.etatTourneesCalculees);
                break;
            case DELETE:
                Livreur livreur = c.vue.comboboxLivreur.getValue();
                this.supprimerLivraison(c);
                this.sortieDeSelectionDemande(c,true);
                this.afficherTournee(c,livreur.getTournee());
        }
    }
}
