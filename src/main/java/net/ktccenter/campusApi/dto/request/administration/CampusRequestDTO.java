package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class CampusRequestDTO {
  private Long id;
  @NotBlank(message = "Le code du campus est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "le libelle du campus est obligatoire")
  private String libelle;
  @NotBlank(message = "l'adresse du campus est obligatoire")
  private String adresse;
  private Long brancheId;
}
