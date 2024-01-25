package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportCampusRequestDTO {
  @NotBlank(message = "Le code du campus est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "le libelle du campus est obligatoire")
  private String libelle;
  @NotBlank(message = "l'adresse du campus est obligatoire")
  private String adresse;
  private String brancheCode;
}
