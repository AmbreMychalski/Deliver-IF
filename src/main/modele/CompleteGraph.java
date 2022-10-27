package main.modele;

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
    private Map<Integer, DemandeLivraison > idIndexToDemandeLivraison =  new HashMap<Integer, DemandeLivraison>();
    int nbVertices;
    float[][] cost;
    
    /**
     * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
     * @param nbVertices
     */
    public CompleteGraph(List<DemandeLivraison> demandesLivraisons,  Plan plan, Intersection entrepot){
        
        nbVertices = demandesLivraisons.size()+1;
        cost = new float[demandesLivraisons.size()+1][demandesLivraisons.size()+1];
        for(float[] row : cost) {
            Arrays.fill(row, -1.0f);
        }
         
        Integer index = 1;      
        ArrayList<Intersection> listIntersection = new ArrayList<Intersection>();
        for(DemandeLivraison dl: demandesLivraisons) {
            idDemandeLivraisonToIndex.put(dl, index);
            idIndexToDemandeLivraison.put(index, dl);
            listIntersection.add(dl.getIntersection());
            index++;
        }
        listIntersection.add(entrepot);
        
        HashMap<Intersection, Float> plusCourtsChemins= plan.calculerPlusCourtsChemins(listIntersection, entrepot);
        for(DemandeLivraison dl : demandesLivraisons ) {         
            index = idDemandeLivraisonToIndex.get(dl);
            cost[0][index]=plusCourtsChemins.get(dl.getIntersection());                      
        } 
        
        // Pour chaque points de livraisons calcul les plus courts chemins à tous les autres points de livraisons       
        for(DemandeLivraison currentDl: demandesLivraisons) {
            plusCourtsChemins = plan.calculerPlusCourtsChemins(listIntersection, currentDl.getIntersection());
            Integer currentIndex =  idDemandeLivraisonToIndex.get(currentDl);           
            for(DemandeLivraison dl : demandesLivraisons ) {
                if(dl != currentDl ) {
                    if(currentDl.getPlageHoraire().getFin()<=dl.getPlageHoraire().getDebut() || currentDl.getPlageHoraire().getDebut()==dl.getPlageHoraire().getDebut()) {
                        index = idDemandeLivraisonToIndex.get(dl);
                        cost[currentIndex][index]=plusCourtsChemins.get(dl.getIntersection());  
                    }   
                }
            }
            cost[currentIndex][0]=plusCourtsChemins.get(entrepot);
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
    
    public void printGraph() {
        for(int i=0; i<nbVertices; i++) {
            for(int j=0; j<nbVertices; j++) {
                System.out.print(cost[i][j]+" ");
            } 
            System.out.println();
        }
    }

}
