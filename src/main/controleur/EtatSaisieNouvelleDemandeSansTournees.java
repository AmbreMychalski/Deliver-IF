package controleur;

import javafx.scene.input.MouseEvent;
/**
 * Classe implémentant l'état où aucune tournée n'a été calculée, et où l'on
 * saisit une nouvelle demande
 */
public class EtatSaisieNouvelleDemandeSansTournees extends Etat{

    public EtatSaisieNouvelleDemandeSansTournees() {
        super.message = "Cliquez sur le plan pour choisir une "
                + "intersection, puis choisissez la plage horaire";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, false);
    }

    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        boolean ajoutOK = this.validerAjoutDemande(c);

        if(ajoutOK) {
            c.changementEtat(c.etatAvecDemande);
        }
    }

    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);

        if (c.vue.comboboxLivreur.getValue().getDemandeLivraisons().size() == 0) {
            c.changementEtat(c.etatSansDemande);
        } else {
            c.changementEtat(c.etatAvecDemande);
        }
    }
}
