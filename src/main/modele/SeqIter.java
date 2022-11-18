package modele;

import java.util.Collection;
import java.util.Iterator;

/**
 * Classe implémentant les itérations
 */
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

	/**
	 * Permet de vérifier s'il reste des candidats
	 * @return true s'il reste un élément suivant, false sinon
	 */
	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	/**
	 * Permet de récupérer le prochaine élément à traiter
	 * @return le prochain candidat
	 */
	@Override
	public Integer next() {
		nbCandidats--;
		return candidats[nbCandidats];
	}

	@Override
	public void remove() {}
}
