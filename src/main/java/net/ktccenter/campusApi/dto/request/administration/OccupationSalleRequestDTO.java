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
public class OccupationSalleRequestDTO {
  private Long id;
  @NotBlank(message = "Le code du diplome est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  private Boolean estOccupee;
  private Long plageHoraireId;
  private Long salleId;
}
