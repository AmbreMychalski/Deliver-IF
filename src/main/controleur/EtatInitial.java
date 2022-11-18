package controleur;

import exception.FichierNonConformeException;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import modele.*;

import java.io.File;
import java.util.ArrayList;

import static controleur.ControleurFenetrePrincipale.LOGGER;

/**
 * Classe implémentant l'état de base de l'application, après le lancement,
 * quand aucun plan n'a été chargé
 */
public class EtatInitial extends Etat {

    public EtatInitial() {
        super.message = "Chargez un plan";
    }
    public void chargerPlan(ControleurFenetrePrincipale c) throws Exception {
        this.chargerNouveauPlan(c);
    }
}
