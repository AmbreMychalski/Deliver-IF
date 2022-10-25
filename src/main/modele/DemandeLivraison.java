package main.modele;

import java.util.Objects;

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
