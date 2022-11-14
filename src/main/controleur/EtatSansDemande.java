package controleur;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import static controleur.ControleurFenetrePrincipale.LOGGER;

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
        this.naviguerSurPlan(c, event, false);
    }
    public void chargerListeDemandes(ControleurFenetrePrincipale c) throws Exception {
        try{
            this.chargerDemandes(c);
            c.changementEtat(c.etatAvecDemande);

        }catch (Exception ex){
            ex.printStackTrace();
            c.changementEtat(c.etatSansDemande);
            throw new Exception(ex);
        }
    }

    @Override
    public void clicSurLivreur(ControleurFenetrePrincipale c) {
        this.changementLivreur(c);
    }
}
