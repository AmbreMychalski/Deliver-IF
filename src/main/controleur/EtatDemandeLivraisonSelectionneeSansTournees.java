package controleur;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;

public class EtatDemandeLivraisonSelectionneeSansTournees extends Etat {

    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.sortieDeSelectionDemande(c);
        c.etatCourant = c.etatAvecDemande;
        c.buttonCalculerTournees.setDisable(false);
        c.buttonChargerDemandes.setDisable(false);
    }
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        this.selectionnerDemande(c);

    }
    
    public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {}
    
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        this.supprimerDemandeLivraison(c);
        this.sortieDeSelectionDemande(c);
        if(c.journee.getDemandesLivraison().size() > 0){
            c.etatCourant = c.etatAvecDemande;
            c.buttonCalculerTournees.setDisable(false);
            c.buttonChargerDemandes.setDisable(false);
        } else {
            c.etatCourant = c.etatSansDemande;
        }

    }

    
    public void modifierDemande(ControleurFenetrePrincipale c) {

        this.modifierDemandeApresSelection(c);
        c.etatCourant = c.etatModifierDemandeLivraisonSansTournees;
    }
    
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {

        if(ke.getCode()== KeyCode.ESCAPE) {
            this.sortieDeSelectionDemande(c);
            c.etatCourant = c.etatAvecDemande;
            c.buttonCalculerTournees.setDisable(false);
            c.buttonChargerDemandes.setDisable(false);
        }
    }

}
