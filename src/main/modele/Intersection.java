package modele;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Intersection {
	private Long idIntersection;
	private float latitude;
	private float longitude;

    /**
     * Permet de récupérer le hashCode pour les valeurs indiquées.
     * @return le hashCode de l'ensemble des objets considérés.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idIntersection, latitude, longitude);
    }

    /**
     * Vérifie l'égalité entre l'objet courant et celui passé en paramètre.
     * @param obj L'objet auquel on compare l'objet courant
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

        Intersection other = (Intersection) obj;

        return Objects.equals(idIntersection, other.idIntersection)
                && Float.floatToIntBits(latitude) == Float.floatToIntBits(other.latitude)
                && Float.floatToIntBits(longitude) == Float.floatToIntBits(other.longitude);
    }
}
