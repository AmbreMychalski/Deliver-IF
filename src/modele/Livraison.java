package modele;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Livraison {
	private DemandeLivraison demandeLivraison;
	private Date date;
}
