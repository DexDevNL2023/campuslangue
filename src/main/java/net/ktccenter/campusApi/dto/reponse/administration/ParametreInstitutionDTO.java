package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ParametreInstitutionDTO extends AbstractDTO {
    private String bareme;
    private String devise;
    private Integer dureeCours;
}