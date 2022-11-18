package modele;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe implémentant un livreur. Le nombre de livreurs est static, et chaque
 * livreur possède un numéro, une tournée (null si elle n'a pas été calculée),
 * une liste de demandes de livraison et de livraisons associées.
 */
@Getter
@Setter
public class Livreur extends Observable {
    static int nbLivreur = 0;
    private int numero;
    private Tournee tournee;
    private List<Livraison> livraisons = new ArrayList<>();
    private List<DemandeLivraison> demandeLivraisons = new ArrayList<>();

    /**
     * Constructeur de "copie" de la classe Livreur.
     * @param tournee La tournée que l'on veut attribuer au livreur que l'on créé
     */
    public Livreur(Tournee tournee) {
        nbLivreur++;
        numero = nbLivreur;
        this.tournee = tournee;
        livraisons = tournee.getLivraisons();
    }

    /**
     * Constructeur de la classe Livreur.  Incrémente également le nombre de
     * livreurs et affecte un numéro au livreur.
     */
    public Livreur() {
        nbLivreur++;
        numero = nbLivreur;
    }

    /**
     * Permet d'afficher le numéro du livreur courant.
     * @return Le numéro du livreur considéré
     */
    public String toString(){
        return Integer.toString(numero);
    }

    /**
     * Permet d'ajouter une demande de livraison au livreur courant.
     * @param livr La demande de livraison que l'on veut ajouter
     */
    public void ajouterDemandeLivraison(DemandeLivraison livr) {
        this.getDemandeLivraisons().add(livr);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    /**
     * Permet de modifier la tournée courant, en la remplaçant par la tournée
     * en paramètre.
     * @param tournee La tournée que l'on veut copier
     */
    public void modifierTournee(Tournee tournee) {
        this.tournee = tournee;
        this.livraisons = tournee.getLivraisons();
        notifierObservateurs("ModificationTournee");
    }

    /**
     * Permet de notifier les observateurs de l'objet en paramètre.
     * @param arg L'objet que l'on veut notifier
     */
    public void notifierObservateurs(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Permet d'ajouter une observateur sur l'objet courant.
     * @param obj L'observateur que l'on veut ajouter
     */
    public void ajouterObservateur(Observer obj) {
        addObserver(obj);
    }

    /**
     * Permet de supprimer la tournée associée au livreur.
     */
    public void supprimerTournee() {
        this.tournee = null;
        this.livraisons = new ArrayList<>();
        this.demandeLivraisons = new ArrayList<>();
        notifierObservateurs("SuppressionTournee");
    }

    /**
     * Permet de supprimer la demande de livraison en paramètre. Notifie les
     * observateurs afin de déclencher certaines actions.
     * @param ligne La demande de livraison à supprimer
     */
    public void supprimerDemandeLivraison(DemandeLivraison ligne) {
        this.demandeLivraisons.remove(ligne);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    /**
     * Permet de supprimer la livraison en paramètre, ainsi que la demande de
     * livraison associée.
     * @param livr La livraison que l'on veut supprimer
     */
    public void supprimerLivraison(Livraison livr) {
        this.livraisons.remove(livr);
        this.demandeLivraisons.remove(livr.getDemandeLivraison());
        notifierObservateurs("ModificationTournee");
    }

    /**
     * Permet d'ajouter une livraison à la tournée du livreur.
     * @param index L'index auquel on veut ajouter la livraison
     * @param livr La livraison que l'on veut ajouter
     */
    public void ajouterLivraisonTournee(int index, Livraison livr) {
        this.tournee.getLivraisons().add(index, livr);
        notifierObservateurs("ModificationTournee");
    }

    /**
     * Permet de modifier une demande de livraison associée au livreur courant
     * @param demande La demande à modifier
     * @param intersection La valeur de la nouvelle intersection (peut être null)
     * @param plageHoraire La valeur de la nouvelle plage horaire (peut être null)
     */
    public void modifierDemandeLivraison(DemandeLivraison demande,
                                         Intersection intersection,
                                         PlageHoraire plageHoraire) {
        demande.modifierDemandeLivraison(intersection, plageHoraire);
        notifierObservateurs("ModificationAjoutSuppressionDemandeLivraison");
    }

    public static void reinitializeNbLivreurs(){
        nbLivreur = 0;
    }
}


