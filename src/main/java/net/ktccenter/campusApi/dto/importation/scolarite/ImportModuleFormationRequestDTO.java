package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportModuleFormationRequestDTO {
  @NotBlank(message = "Le code du module formation est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "le libelle du module formation est obligatoire")
  private String libelle;
  private String niveauCode;
}
