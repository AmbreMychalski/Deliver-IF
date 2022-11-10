package controleur;

import javafx.scene.input.MouseEvent;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;

public class EtatSaisieNouvelleDemandeAvecTournees extends Etat {
    public EtatSaisieNouvelleDemandeAvecTournees() {
        super.message = "Cliquez sur le plan pour choisir une " +
                "intersection, puis choisissez la plage horaire";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        String champIdentifiant = c.vue.textfieldIdentifiantIntersection.getText();
        PlageHoraire plageHoraire = c.vue.comboboxPlageHoraire.getValue();
        if (!champIdentifiant.isEmpty() && plageHoraire != null) {
            Intersection intersection = c.journee.getPlan().getIntersections()
                    .get(Long.parseLong(champIdentifiant));
            if (c.journee.getPlan().estLivrable(intersection)) {
                DemandeLivraison demande = new DemandeLivraison(intersection, plageHoraire);
                int livreur = c.vue.comboboxLivreur.getValue();
                c.journee.ajouterDemandeLivraisonTournee(demande, c.journee.getTournees().get(livreur - 1).getLivraisons().get(0));
                c.changementEtat(c.etatTourneesCalculees);
                this.afficherTournee(c, c.journee.getTournees().get(livreur - 1));
            }
        }
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        c.changementEtat(c.etatTourneesCalculees);
    }
   

}
