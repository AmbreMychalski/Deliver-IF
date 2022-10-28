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
	
	
	public  void modifierDemandeLivraison(Intersection intersection, PlageHoraire plageHoraire) {
	    if(intersection != null) {
	        this.setIntersection(intersection);
	    }
	    if(plageHoraire != null) {
	        this.setPlageHoraire(plageHoraire);
	    }
	}
	
	public Long getIdIntersection() {
	    return intersection.getIdIntersection();
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(intersection, plageHoraire);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DemandeLivraison other = (DemandeLivraison) obj;
        return Objects.equals(intersection, other.intersection) && Objects.equals(plageHoraire, other.plageHoraire);
    }
	
	
    
    
}
