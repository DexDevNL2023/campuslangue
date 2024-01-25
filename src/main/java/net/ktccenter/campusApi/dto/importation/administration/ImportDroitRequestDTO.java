package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportDroitRequestDTO {
    @NotBlank(message = "le libelle du droit est obligatoire")
    private String libelle;
    @NotBlank(message = "La clé du droit est obligatoire")
    @Size(min = 2, message = "La clé doit être d'au moins 2 caractères")
    private String key;
    @NotBlank(message = "La verbe du droit est obligatoire")
    private String verbe;
    private String description;
    private String moduleName;
}
