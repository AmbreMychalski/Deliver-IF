package modele;

import java.util.Objects;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PlageHoraire {
    int debut;
    int fin;
    Color couleur;

    /**
     * Constructeur. Associe une couleur selon la plage horaire donnée.
     * @param debut L'heure de début de la plage
     * @param fin L'heure de fin de la plage
     */
    public PlageHoraire(int debut, int fin) {
        this.debut = debut;
        this.fin = fin;

        switch (debut) {
            case 8 :
                this.couleur = Color.BLUEVIOLET;
                break;
            case 9:
                this.couleur = Color.DARKCYAN;
                break;
            case 10:
                this.couleur = Color.DARKORANGE;
                break;
            case 11:
                this.couleur = Color.DEEPPINK;
                break;
            default:
                this.couleur = Color.GREEN;
        }
    }

    /**
     * Surcharge de la classe toString de la plage horaire.
     * @return La chaîne de caractères de la plage horaire.
     */
    @Override
    public String toString() {
        return "De " + debut + "h à " + fin + "h";
    }

    /**
     * Permet de récupérer le hashCode d'une plage horaire.
     * @return Le hashCode pour le début et la fin de la plage horaire.
     */
    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }

    /**
     * Permet de vérifie l'égalité entre deux plages horaires.
     * @param obj L'objet par rapport auquel on veut comparer la plage horaire courante.
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        PlageHoraire other = (PlageHoraire) obj;

        return debut == other.debut && fin == other.fin;
    }
}
