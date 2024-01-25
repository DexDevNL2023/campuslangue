package net.ktccenter.campusApi.dto.importation.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportUniteRequestDTO {
  @NotBlank(message = "Le code de l'unite d'evaluation est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "le libelle de l'unite d'evaluation est obligatoire")
  private String libelle;
  private Float noteAdmission;
  private String niveauCode;
}
