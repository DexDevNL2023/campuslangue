package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class ImportRoleRequestDTO {
    @NotBlank(message = "le libelle du role est obligatoire")
    private String libelle;
    private Boolean isSuper;
    private Boolean isGrant = false;
}
