package main.modele;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor

public class PlageHoraire {
    int debut;
    int fin;
    
    @Override
    public String toString() {
        return "De "+debut+"h à "+fin+"h";
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlageHoraire other = (PlageHoraire) obj;
        return debut == other.debut && fin == other.fin;
    }
    
    

}
