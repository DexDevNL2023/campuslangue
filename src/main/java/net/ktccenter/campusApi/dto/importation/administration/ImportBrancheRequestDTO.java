package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ImportBrancheRequestDTO {
  @NotBlank(message = "Le code de la branche est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "Le ville de la branche est obligatoire")
  private String ville;
  @NotBlank(message = "Le telephone est obligatoire")
  private String telephone;
  private String email;
}
