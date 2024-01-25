package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter

@Getter

@NoArgsConstructor
public class ImportDiplomeRequestDTO {
    @NotBlank(message = "Le code du diplome est obligatoire")
    @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
    private String code;
    @NotBlank(message = "le libelle du diplome est obligatoire")
    private String libelle;
}
