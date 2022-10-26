package main.controleur;

import java.io.File;

import javafx.stage.FileChooser;
import main.modele.Plan;
import main.modele.Segment;

public class EtatInitial implements Etat{
    public void chargerPlan(ControleurFenetrePrincipale c) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(".\\data"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml", "*.XML"));
        fileChooser.setTitle("Charger un plan");
        File fichier = fileChooser.showOpenDialog(c.stage);
        System.out.println("Fichier choisi = " + fichier.getAbsolutePath());

        c.planCharge = new Plan(fichier);
        c.journee.setPlan(c.planCharge);
        
        System.out.println("Plan chargé : " + c.planCharge);

        c.latMax = c.planCharge.getIntersections().values().stream()
                .map(intersection -> intersection.getLatitude())
                .max((a, b) -> Float.compare(a, b)).orElse(0f);
        c.latMin = c.planCharge.getIntersections().values().stream()
                .map(intersection -> intersection.getLatitude())
                .min((a, b) -> Float.compare(a, b)).orElse(0f);
        c.longMax = c.planCharge.getIntersections().values().stream()
                .map(intersection -> intersection.getLongitude())
                .max((a, b) -> Float.compare(a, b)).orElse(0f);
        c.longMin = c.planCharge.getIntersections().values().stream()
                .map(intersection -> intersection.getLongitude())
                .min((a, b) -> Float.compare(a, b)).orElse(0f);

        c.largeurPlan = c.longMax - c.longMin;
        c.hauteurPlan = c.latMax - c.latMin;

        c.echelle = Math.min(c.canvasPlan.getWidth() / c.largeurPlan,
                c.canvasPlan.getHeight() / c.hauteurPlan);

        for (Segment segment : c.planCharge.getSegments()) {
            c.dessinerSegmentLatLong(segment.getOrigine().getLatitude(), 
                    segment.getOrigine().getLongitude(), 
                    segment.getDestination().getLatitude(), 
                    segment.getDestination().getLongitude(),
                    c.COULEUR_SEGMENT);
        }

        c.dessinerIntersectionLatLong(c.canvasPlan.getGraphicsContext2D(),
                                        c.planCharge.getEntrepot().getLatitude(), 
                                        c.planCharge.getEntrepot().getLongitude(), 
                                        c.COULEUR_DEPOT,
                                        c.TAILLE_CERCLE_INTERSECTION,
                                        true,
                                        "Cercle");
        c.etatCourant = c.etatSansDemande;
    }
    
    public void ajouterDemande(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurPlan(ControleurFenetrePrincipale c) {}
    
    public void clicGaucheSurTableau(ControleurFenetrePrincipale c) {}
    
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
}
