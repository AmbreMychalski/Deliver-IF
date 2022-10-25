package main.modele;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TableRow {
    private Long idIntersection;
    private PlageHoraire plageHoraire;
    public TableRow(DemandeLivraison demande) {
        this.idIntersection =demande.getIntersection().getIdIntersection();
        this.plageHoraire = demande.getPlageHoraire();
    }
    public Long getIdIntersection() {
        return this.idIntersection;
    }
    public PlageHoraire getPlageHoraire() {
        return this.plageHoraire;
    }
    public void setIdIntersection(Long value) {
        this.idIntersection = value;
    }
    public void setPlageHoraire(PlageHoraire value) {
        this.plageHoraire = value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idIntersection, plageHoraire);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableRow other = (TableRow) obj;
        return Objects.equals(idIntersection, other.idIntersection) && Objects.equals(plageHoraire, other.plageHoraire);
    }
    
    
}