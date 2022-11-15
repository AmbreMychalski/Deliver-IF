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
    @Override
    public String toString() {
        return "De " + debut + "h à " + fin + "h";
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }

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
