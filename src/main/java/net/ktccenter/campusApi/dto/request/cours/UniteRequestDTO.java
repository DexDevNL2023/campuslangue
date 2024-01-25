package net.ktccenter.campusApi.dto.request.cours;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class UniteRequestDTO {
  private Long id;
  @NotBlank(message = "Le code de l'unite d'evaluation est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "le libelle de l'unite d'evaluation est obligatoire")
  private String libelle;
  private Float noteAdmission;
  private Long niveauId;
}
