package net.ktccenter.campusApi.dto.importation.scolarite;

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
public class ImportVagueRequestDTO {
  @NotBlank(message = "Le code de la vague est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
}
