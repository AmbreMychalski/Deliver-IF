package main.controleur;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.modele.DemandeLivraison;
import main.modele.Segment;
import main.modele.Tournee;
import main.modele.Trajet;

public class EtatDemandeLivraisonSelectionneeAvecTournees implements Etat {

    public void chargerPlan(ControleurFenetrePrincipale c) {}
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c) {
        c.buttonModifierLivraison.setDisable(true);
        c.buttonSupprimerLivraison.setDisable(true);
        c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.mettreAJourCanvasDemande();
        c.textfieldIdentifiantIntersectionSelection.setText("");
        c.textfieldPlageHoraire.setText("");

        c.etatCourant = c.etatTourneesCalculees;
     
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
            
            c.journee.supprimerDemandeLivraison(ligne);
            c.tableViewDemandesLivraison.getItems().remove(ligne);
            c.tableViewDemandesLivraison.refresh();
            c.textfieldIdentifiantIntersectionSelection.setText("");
            c.textfieldPlageHoraire.setText("");
            c.mettreAJourCanvasDemande();
        }

        c.buttonAutoriserAjouterLivraison.setDisable(false);
        c.buttonModifierLivraison.setDisable(true);
        c.buttonSupprimerLivraison.setDisable(true);
        
        if(c.journee.getDemandesLivraison().size() != 0) {
            c.journee.calculerTournee();
            GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
            gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
            Tournee tournee = c.journee.getTournees().get(c.journee.getTournees().size()-1);
            List<Trajet> trajets = tournee.getTrajets();
            for(Trajet trajet : trajets) {
                List<Segment> segments = trajet.getSegments(); 
                for(Segment segment : segments) {
                    c.dessinerTrajetLatLong((double)segment.getOrigine().getLatitude(),
                            (double)segment.getOrigine().getLongitude(),
                            (double)segment.getDestination().getLatitude(),
                            (double)segment.getDestination().getLongitude());
                }
            }
            
            c.etatCourant = c.etatTourneesCalculees;
        } else {
            GraphicsContext gc = c.canvasPlanTrajet.getGraphicsContext2D();
            gc.clearRect(0, 0, c.canvasPlanTrajet.getWidth(), c.canvasPlanTrajet.getHeight());
            c.etatCourant = c.etatSansDemande;
        }
        
    }
    
    public void sauvegarderDemandes(ControleurFenetrePrincipale c) {}
    
    public void calculerTournees(ControleurFenetrePrincipale c) {}
    
    public void afficherFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void sauvegarderFeuillesRoute(ControleurFenetrePrincipale c) {}
    
    public void fermerFenetre(ControleurFenetrePrincipale c) {}
    
    public void quitterLogiciel(ControleurFenetrePrincipale c) {}
    
    public void modifierDemande(ControleurFenetrePrincipale c) {}
    
    public  void touchePressee(ControleurFenetrePrincipale c, KeyEvent ke) {
        if(ke.getCode()== KeyCode.ESCAPE) {

            c.buttonModifierLivraison.setDisable(true);
            c.buttonSupprimerLivraison.setDisable(true);
            
            c.buttonAutoriserAjouterLivraison.setDisable(false);
            c.mettreAJourCanvasDemande();
            c.textfieldIdentifiantIntersectionSelection.setText("");
            c.textfieldPlageHoraire.setText("");
          
            c.etatCourant = c.etatTourneesCalculees;
        
        }
    }

}
