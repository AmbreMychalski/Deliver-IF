package modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteGraph implements Graph {
    private static final int MAX_COST = 40;
    private static final int MIN_COST = 10;
    
    private Map<DemandeLivraison, Integer> idDemandeLivraisonToIndex =  new HashMap<DemandeLivraison, Integer>();
    int nbVertices;
    float[][] cost;
    
    /**
     * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
     * @param nbVertices
     */
    public CompleteGraph(List<DemandeLivraison> demandesLivraisons,  Plan plan){
        
        nbVertices = demandesLivraisons.size();
        cost = new float[demandesLivraisons.size()][demandesLivraisons.size()];
        for(float[] row : cost) {
            Arrays.fill(row, -1.0f);
        }
        
        
        Integer index = 0;      
        ArrayList<Intersection> listIntersection = new ArrayList<Intersection>();
        for(DemandeLivraison dl: demandesLivraisons) {
            idDemandeLivraisonToIndex.put(dl, index);
            listIntersection.add(dl.getIntersection());
            index++;
        }
        
        
        // Pour chaque points de livraisons calcul les plus courts chemins à tous les autres points de livraisons       
        HashMap<Intersection, Float> plusCourtsChemins;
        for(DemandeLivraison currentDl: demandesLivraisons) {
            plusCourtsChemins = plan.calculerPlusCourtsChemins(listIntersection, currentDl.getIntersection());
            Integer currentIndex =  idDemandeLivraisonToIndex.get(currentDl);           
            for(DemandeLivraison dl : demandesLivraisons ) {
                if(dl != currentDl ) {
                    if(currentDl.getFinPlageHoraire()==dl.getDebutPlageHoraire() || currentDl.getDebutPlageHoraire()==dl.getDebutPlageHoraire()) {
                        index = idDemandeLivraisonToIndex.get(dl);
                        cost[currentIndex][index]=plusCourtsChemins.get(dl.getIntersection());  
                    }   
                }
            }
        }
        
    }

    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    @Override
    public float getCost(int i, int j) {
        if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
            return -1;
        return cost[i][j];
    }

    @Override
    public boolean isArc(int i, int j) {
        if (cost[i][j]<0) {
            return false;
        }
        return true;
    }

}
