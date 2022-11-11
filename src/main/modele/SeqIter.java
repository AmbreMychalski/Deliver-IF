package modele;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Integer> {
	private Integer[] candidats;
	private int nbCandidats;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param nonVisites
	 * @param sommetCourant
	 * @param g
	 */
	public SeqIter(Collection<Integer> nonVisites, int sommetCourant, Graphe g) {
		this.candidats = new Integer[nonVisites.size()];

		for (Integer s : nonVisites) {
			if (g.estUnArc(sommetCourant, s)) {
				candidats[nbCandidats++] = s;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	@Override
	public Integer next() {
		nbCandidats--;
		return candidats[nbCandidats];
	}

	@Override
	public void remove() {}
}
