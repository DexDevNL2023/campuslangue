package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.ParametreInstitution;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LiteParametreInstitutionDTO {
    private Long id;
    private int bareme;
    private String devise;
    private double dureeCours;
    private int pourcentageTestModule;

    public LiteParametreInstitutionDTO(ParametreInstitution parametres) {
        this.id = parametres.getId();
        this.bareme = parametres.getBareme();
        this.devise = parametres.getDevise();
        this.dureeCours = parametres.getDureeCours();
    }
}
