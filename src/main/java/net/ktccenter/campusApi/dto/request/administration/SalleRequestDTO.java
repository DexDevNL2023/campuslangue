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
public class SalleRequestDTO {
  private Long id;
  @NotBlank(message = "Le de la salle est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "l'intitule de la salle est obligatoire")
  private String libelle;
  private Integer capacite;
  private Long campusId;
}
