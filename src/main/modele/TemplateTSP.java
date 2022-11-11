package modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Integer[] meilleureSolution;
	protected Graphe g;
	private float coutMeilleureSolution;
	private int limiteTemps;
	private long heureDebut;
	
	public void searchSolution(int timeLimit, Graphe g) {
		if (timeLimit <= 0) {
			return;
		}

		heureDebut = System.currentTimeMillis();
		this.limiteTemps = timeLimit;
		this.g = g;
		meilleureSolution = new Integer[g.getNbSommets()];
		ArrayList<Integer> nonVisites = new ArrayList<Integer>(g.getNbSommets() - 1);

		for (int i = 1; i < g.getNbSommets(); i++) {
			nonVisites.add(i);
		}

		// The first visited vertex is 0
		coutMeilleureSolution = Integer.MAX_VALUE;
		ArrayList<Integer> visited = new ArrayList<Integer>(g.getNbSommets());

		visited.add(0);

		branchAndBound(visited.get(visited.size()-1), nonVisites, visited,
				0);
	}
	
	public Integer getSolution(int i) {
		if (g != null && i >= 0 && i < g.getNbSommets()) {
			return meilleureSolution[i];
		}

		return -1;
	}
	
	public float getSolutionCost() {
		if (g != null) {
			return coutMeilleureSolution;
		}

		return -1;
	}
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract float bound(Integer currentVertex, Collection<Integer> unvisited, float[][] cost);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graphe g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param sommetCourant the last visited vertex
	 * @param nonVisites the set of vertex that have not yet been visited
	 * @param visites the sequence of vertices that have been already visited (including currentVertex)
	 * @param coutCourant the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(int sommetCourant, Collection<Integer> nonVisites,
			Collection<Integer> visites, float coutCourant) {
		if (System.currentTimeMillis() - heureDebut > limiteTemps) {
		    if(coutMeilleureSolution == Integer.MAX_VALUE) {
		        System.out.println("Le TSP n'a pas trouvé de solution");
		    }

		    return;
		}
	    if (nonVisites.size() == 0) {
	    	if (g.estUnArc(sommetCourant,0)) {
	    		if (coutCourant+g.getCout(sommetCourant,0) < coutMeilleureSolution) {
	    			visites.toArray(meilleureSolution);
	    			coutMeilleureSolution = coutCourant+g.getCout(sommetCourant,0);
	    		}
	    	}
	    } else if (coutCourant+bound(sommetCourant,nonVisites,g.getMatriceCouts()) < coutMeilleureSolution) {
	        Iterator<Integer> it = iterator(sommetCourant, nonVisites, g);

	        while (it.hasNext()) {
	        	Integer sommetSuivant = it.next();
	        	visites.add(sommetSuivant);
	            nonVisites.remove(sommetSuivant);
	            branchAndBound(sommetSuivant, nonVisites, visites,
	            		coutCourant + g.getCout(sommetCourant, sommetSuivant));
	            visites.remove(sommetSuivant);
	            nonVisites.add(sommetSuivant);
	        }	    
	    }
	}

}
