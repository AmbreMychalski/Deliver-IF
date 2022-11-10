package modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {
	@Override
	protected float bound(Integer currentVertex, Collection<Integer> unvisited, float[][] cost) {

		float res = 0;
		Collection<Integer> boundCollection = new ArrayList<Integer>(unvisited);

		boundCollection.add(0);
		boundCollection.add(currentVertex);

		for(Integer i : boundCollection) {
			float min = Float.MAX_VALUE;
			for(Integer j : boundCollection) {
				if(cost[i][j] < min && cost[i][j] != -1) {
					min = cost[i][j];
				}
			}
			if(min != Float.MAX_VALUE) {
				res += min;
			}
		}

		return res;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex,
										 Collection<Integer> unvisited,
										 Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
