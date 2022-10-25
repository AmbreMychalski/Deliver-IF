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
public class Intersection {
	private Long idIntersection;
	private float latitude;
	private float longitude;
    @Override
    public int hashCode() {
        return Objects.hash(idIntersection, latitude, longitude);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Intersection other = (Intersection) obj;
        return Objects.equals(idIntersection, other.idIntersection)
                && Float.floatToIntBits(latitude) == Float.floatToIntBits(other.latitude)
                && Float.floatToIntBits(longitude) == Float.floatToIntBits(other.longitude);
    }
	
	
}
