package modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {
	@Override
	protected float bound(Integer sommetCourant, Collection<Integer> nonVisites, float[][] couts) {

		float res = 0;
		Collection<Integer> boundCollection = new ArrayList<Integer>(nonVisites);

		boundCollection.add(0);
		boundCollection.add(sommetCourant);

		for(Integer i : boundCollection) {
			float min = Float.MAX_VALUE;
			for(Integer j : boundCollection) {
				if(couts[i][j] < min && couts[i][j] != -1) {
					min = couts[i][j];
				}
			}
			if(min != Float.MAX_VALUE) {
				res += min;
			}
		}

		return res;
	}

	@Override
	protected Iterator<Integer> iterator(Integer sommetCourant,
										 Collection<Integer> nonVisites,
										 Graphe g) {
		return new SeqIter(nonVisites, sommetCourant, g);
	}

}
