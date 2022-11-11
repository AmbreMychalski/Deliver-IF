package controleur;

import javafx.scene.input.MouseEvent;

import static controleur.ControleurFenetrePrincipale.logger;

public class EtatSansDemande extends Etat{
    public EtatSansDemande() {
        super.message = "Ajoutez ou chargez des demandes de livraison";
    }

    public void chargerPlan(ControleurFenetrePrincipale c)
            throws Exception {
        c.etatInitial.chargerPlan(c);
    }
    public void ajouterDemande(ControleurFenetrePrincipale c) {
        c.vue.comboboxPlageHoraire.setDisable(false);
        c.changementEtat(c.etatSaisieNouvelleDemandeSansTournees);
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception {
        try{
            this.chargerDemandes(c);
            c.changementEtat(c.etatAvecDemande);

        }catch (Exception ex){
            logger.error(ex);
            c.changementEtat(c.etatSansDemande);
            throw new Exception(ex);
        }
    }
}
