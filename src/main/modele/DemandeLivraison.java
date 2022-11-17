package modele;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DemandeLivraison {
    private Intersection intersection;
	private PlageHoraire plageHoraire;

    /**
     * Pour la demande de livraison courante, modifie son intersection et
     * sa plage horaire si celles fournies ne sont pas null.
     * @param intersection La nouvelle intersection (peut être vide)
     * @param plageHoraire La nouvelle plage horaire (peut être vide)
     */
	public  void modifierDemandeLivraison(Intersection intersection,
                                          PlageHoraire plageHoraire) {
	    if(intersection != null) {
	        this.setIntersection(intersection);
	    }

	    if(plageHoraire != null) {
	        this.setPlageHoraire(plageHoraire);
	    }
	}

    /**
     * Permet de récupérer l'ID de l'intersection où se situe la demande
     * @return l'ID de l'intersection où se situe la demande
     */
	public Long getIdIntersection() {
        return intersection.getIdIntersection();
	}

    /**
     * Retourne la valeur de hashage pour l'intersection et la plage horaire
     * données.
     * @return la valeur de hashage de la demande de livraison
     */
    @Override
    public int hashCode() {
        return Objects.hash(intersection, plageHoraire);
    }

    /**
     * Vérifie l'égalité entre l'objet courant et celui en paramètre
     * @param obj L'objet par rapport auquel on compare l'objet courant
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

        DemandeLivraison other = (DemandeLivraison) obj;

        return Objects.equals(intersection, other.intersection)
               && Objects.equals(plageHoraire, other.plageHoraire);
    }
}
