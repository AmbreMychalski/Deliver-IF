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
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {
        this.naviguerSurPlan(c, event);
    }
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.validerAjoutDemande(c);
        this.calculerEtAfficherTournee(c);
        c.etatCourant = c.etatTourneesCalculees;
    }
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {
        this.annulerAjout(c);
        c.etatCourant = c.etatTourneesCalculees;
    }
   

}
