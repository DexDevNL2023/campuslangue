package net.ktccenter.campusApi.dto.importation.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportTestModuleRequestDTO {
  @NotBlank(message = "Le code du test module  est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @DecimalMin(value = "0", inclusive = false)
  @DecimalMax(value = "100", inclusive = false)
  private Double pourcentage;
  private String inscriptionCode;
}
