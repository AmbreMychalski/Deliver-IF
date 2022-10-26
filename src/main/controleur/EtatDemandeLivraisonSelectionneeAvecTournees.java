package main.controleur;

import javafx.scene.input.KeyEvent;
import main.modele.DemandeLivraison;

public class EtatDemandeLivraisonSelectionneeAvecTournees implements Etat {

    public void chargerPlan(ControleurFenetrePrincipale c) {}
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.mettreAJourListeDemandes();
            c.dessinerIntersectionLatLong(c.canvasInterieurPlan.getGraphicsContext2D(),
                                        ligne.getIntersection().getLatitude(), 
                                        ligne.getIntersection().getLongitude(),
                                        c.COULEUR_POINT_LIVRAISON_SELECTIONNE, 
                                        c.TAILLE_RECT_PT_LIVRAISON_SELECTIONNE, 
                                        true, 
                                        "Rectangle");
            
            c.titlePaneSelectionDemande.setVisible(true);
            c.textfieldIdentifiantIntersectionSelection.setText(ligne.getIdIntersection().toString());
            c.textfieldPlageHoraire.setText(ligne.getPlageHoraire().toString());
        }
        c.etatCourant = c.etatDemandeLivraisonSelectionneeSansTournees;
    }
    
    public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {}
    
    public void supprimerDemande(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
    
    public void calculerTournees(ControleurFenetrePrincipale c) {}
    
    public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void fermerFenetre(ControleurFenetrePrincipale c) {}
    
    public void quitterLogiciel(ControleurFenetrePrincipale c) {}
    
    public void modifierDemande(ControleurFenetrePrincipale c) {}
    
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {}

}
