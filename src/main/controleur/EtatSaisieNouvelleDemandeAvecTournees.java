package controleur;

import javafx.scene.input.MouseEvent;
import modele.*;

/**
 * Classe implémentant l'état où une tournée a été calculée, et où on souhaite
 * saisir une nouvelle demande
 */
public class EtatSaisieNouvelleDemandeAvecTournees extends Etat {

    public EtatSaisieNouvelleDemandeAvecTournees() {
        super.message = "Cliquez sur le plan pour choisir une "
                + "intersection, puis choisissez la plage horaire";
    }

    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event, true);
    }

    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
        PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();

        if (!champIdentifiant.isEmpty() && plageHoraire != null) {
            Intersection intersection = c.journee.getPlan().getIntersections()
                    .get(Long.parseLong(champIdentifiant));

            if (c.journee.getPlan().estLivrable(intersection)) {
                DemandeLivraison demande = new DemandeLivraison(intersection, plageHoraire);
                Livreur livreur = c.vue.comboboxLivreur.getValue();

                if(livreur == null) {
                    livreur = c.journee.getLivreurs().get(0);
                }

                c.vue.tableViewDemandesLivraison.setDisable(false);
                c.vue.tableViewLivraisons.setDisable(false);
                livreur.ajouterDemandeLivraison(demande);
                c.changementEtat(c.etatSelectionLivraisonPourNouvelleDemande);
                c.vue.afficherDemandesLivraison(livreur, true);
                c.vue.afficherLivraisons(livreur, true);
                c.vue.dessinerDemandeLivraison(c.vue.canvasIntersectionsLivraisons.getGraphicsContext2D(), demande);
            } else {
                c.vue.labelGuideUtilisateur.setText("Veuillez saisir toutes les informations");
            }
        } else {
            c.vue.labelGuideUtilisateur.setText("Veuillez saisir toutes les informations");
        }
    }

    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        c.changementEtat(c.etatTourneesCalculees);
    }
}
