package controleur;

import exception.FichierNonConformeException;
import javafx.scene.input.MouseEvent;

public class EtatSansDemande extends Etat{
    public EtatSansDemande() {
        super.message = "Ajoutez ou chargez des demandes de livraison";
    }

    public void chargerPlan(ControleurFenetrePrincipale c)
            throws FichierNonConformeException {
        c.etatInitial.chargerPlan(c);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.buttonValiderLivraison.setDisable(false);
        c.vue.buttonAnnulerLivraison.setDisable(false);
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.vue.buttonChargerDemandes.setDisable(true);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {
        this.chargerDemandes(c);
        c.changementEtat(c.etatAvecDemande);
        c.vue.buttonCalculerTournees.setDisable(false);
        c.vue.buttonSauvegarderDemandes.setDisable(false);
    }
}
