package modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Intersection {
	private Long idIntersection;
	private float latitude;
	private float longitude;
}
