package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.entities.administration.Salle;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class SalleByBranchDTO {
    private Long id;
    private String code;
    private String libelle;
    private Integer capacite;
    private Set<LiteOccupationSalleDTO> occupations = new HashSet<>();

    public SalleByBranchDTO(Salle salle) {
        this.id = salle.getId();
        this.code = salle.getCode();
        this.libelle = salle.getLibelle();
        this.capacite = salle.getCapacite();
    }
}
