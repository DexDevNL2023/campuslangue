package net.ktccenter.campusApi.dto.importation.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImportOccupationSalleRequestDTO {
  @NotBlank(message = "Le code du diplome est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  private Boolean estOccupee;
  private String plageHoraireCode;
  private String salleCode;
}
