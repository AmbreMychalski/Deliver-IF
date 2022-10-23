package modele;

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
}
