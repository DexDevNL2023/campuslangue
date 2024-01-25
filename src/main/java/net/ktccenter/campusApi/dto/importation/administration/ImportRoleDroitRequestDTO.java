package net.ktccenter.campusApi.dto.importation.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImportRoleDroitRequestDTO {
    private String roleLibelle;
    private String droitLibelle;
    private Boolean hasDroit;
}
