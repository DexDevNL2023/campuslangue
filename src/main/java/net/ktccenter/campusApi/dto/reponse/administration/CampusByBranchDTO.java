package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Campus;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class CampusByBranchDTO {
    private Long id;
    private String code;
    private String libelle;
    private String adresse;
    private Set<SalleByBranchDTO> salles = new HashSet<>();

    public CampusByBranchDTO(Campus campus) {
        this.id = campus.getId();
        this.code = campus.getCode();
        this.libelle = campus.getLibelle();
        this.adresse = campus.getAdresse();
    }
}
