package main.controleur;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.modele.DemandeLivraison;

public class EtatDemandeLivraisonSelectionneeSansTournees implements Etat {

    public void chargerPlan(ControleurFenetrePrincipale c) {}
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c, MouseEvent event) {

        c.buttonModifierLivraison.setDisable(true);
        c.buttonSupprimerLivraison.setDisable(true);
        c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.mettreAJourCanvasDemande();
        c.textfieldIdentifiantIntersectionSelection.setText("");
        c.textfieldPlageHoraire.setText("");
        if (c.journee.getDemandesLivraison().size() == 0) {
            c.etatCourant = c.etatSansDemande;
        } else {
            c.etatCourant = c.etatAvecDemande;
        }

    }
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {
        DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if (ligne != null) {
            c.mettreAJourCanvasDemande();
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
    }
    
    public void choixPlageHoraire(ControleurFenetrePrincipale c) {}
    
    public void validerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void annulerAjouterOuModifier(ControleurFenetrePrincipale c) {}
    
    public void chargerListeDemandes(ControleurFenetrePrincipale c) {}
    
    public void supprimerDemande(ControleurFenetrePrincipale c) {
        DemandeLivraison ligne = c.tableViewDemandesLivraison.getSelectionModel().getSelectedItem();
        if(ligne != null) {
            c.mettreAJourCanvasDemande();
            c.journee.supprimerDemandeLivraison(ligne);
            c.tableViewDemandesLivraison.getItems().remove(ligne);
            c.tableViewDemandesLivraison.refresh();
            c.textfieldIdentifiantIntersectionSelection.setText("");
            c.textfieldPlageHoraire.setText("");
        }
        if (c.journee.getDemandesLivraison().size() == 0) {
            c.etatCourant = c.etatSansDemande;
        } else {
            c.etatCourant = c.etatAvecDemande;
        }

        c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.buttonModifierLivraison.setDisable(true);
        c.buttonSupprimerLivraison.setDisable(true);
    }
    
    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
    
    public void calculerTournees(ControleurFenetrePrincipale c) {}
    
    public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void fermerFenetre(ControleurFenetrePrincipale c) {}
    
    public void quitterLogiciel(ControleurFenetrePrincipale c) {}
    
    public void modifierDemande(ControleurFenetrePrincipale c) {

        c.buttonModifierLivraison.setDisable(true);
        c.buttonSupprimerLivraison.setDisable(true);
        
        c.buttonAutoriserAjouterLivraison.setDisable(true);
        c.buttonValiderLivraison.setDisable(false);
        c.buttonAnnulerLivraison.setDisable(false);
        c.comboboxPlageHoraire.setDisable(false);
        c.etatCourant = c.etatModifierDemandeLivraisonSansTournees;
    }
    
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        if(ke.getCode()== KeyCode.ESCAPE) {

            c.buttonModifierLivraison.setDisable(true);
            c.buttonSupprimerLivraison.setDisable(true);
            
            c.buttonAutoriserAjouterLivraison.setDisable(false);
            c.mettreAJourCanvasDemande();
            c.textfieldIdentifiantIntersectionSelection.setText("");
            c.textfieldPlageHoraire.setText("");
            if (c.journee.getDemandesLivraison().size() == 0) {
                c.etatCourant = c.etatSansDemande;
            } else {
                c.etatCourant = c.etatAvecDemande;
            }
        }
    }

}
