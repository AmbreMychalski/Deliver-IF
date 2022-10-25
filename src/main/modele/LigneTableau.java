package main.modele;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class LigneTableau {
    private Long idIntersection;
    private PlageHoraire plageHoraire;
    private DemandeLivraison demandeLivraison;
    
    public LigneTableau(DemandeLivraison demande) {
        this.idIntersection =demande.getIntersection().getIdIntersection();
        this.plageHoraire = demande.getPlageHoraire();
        this.demandeLivraison = demande;
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
        LigneTableau other = (LigneTableau) obj;
        return Objects.equals(idIntersection, other.idIntersection) && Objects.equals(plageHoraire, other.plageHoraire);
    }
    
    
}