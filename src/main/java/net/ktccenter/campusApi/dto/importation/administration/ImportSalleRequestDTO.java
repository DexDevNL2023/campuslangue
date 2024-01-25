package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportSalleRequestDTO {
  @NotBlank(message = "Le de la salle est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "l'intitule de la salle est obligatoire")
  private String libelle;
  private Integer capacite;
  private String campusCode;
}
