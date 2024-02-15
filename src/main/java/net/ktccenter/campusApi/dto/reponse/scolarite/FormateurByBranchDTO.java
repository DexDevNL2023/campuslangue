package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleByBranchDTO;
import net.ktccenter.campusApi.entities.administration.Campus;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class FormateurByBranchDTO {
    private Long id;
    private String code;
    private String libelle;
    private String adresse;
    private Set<LiteFormateurDTO> formateurs = new HashSet<>();

    public FormateurByBranchDTO(Campus campus) {
        this.id = campus.getId();
        this.code = campus.getCode();
        this.libelle = campus.getLibelle();
        this.adresse = campus.getAdresse();
    }
}
