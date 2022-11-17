package controleur;

/**
 * Interface de la classe Commande pour le design pattern Command
 */
public interface Commande {
    void undoCommande();
    void doCommande();
}
