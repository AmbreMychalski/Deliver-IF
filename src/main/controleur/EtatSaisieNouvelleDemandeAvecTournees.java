package controleur;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import modele.DemandeLivraison;
import modele.Intersection;
import modele.PlageHoraire;
import modele.Segment;
import modele.Tournee;
import modele.Trajet;

public class EtatSaisieNouvelleDemandeAvecTournees extends Etat {
    public EtatSaisieNouvelleDemandeAvecTournees() {
        super.message = "Cliquez sur le plan pour choisir une " +
                "intersection, puis choisissez la plage horaire";
    }
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        boolean ajoutOK = this.validerAjoutDemande(c);
        if(ajoutOK){
            this.calculerEtAfficherTournee(c);
            c.changementEtat(c.etatTourneesCalculees);
        }
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        c.changementEtat(c.etatTourneesCalculees);
    }
   

}
