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
public class BrancheRequestDTO {
  private Long id;
  @NotBlank(message = "Le code de la branche est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotBlank(message = "Le ville de la branche est obligatoire")
  private String ville;
  @NotBlank(message = "Le telephone est obligatoire")
  private String telephone;
  private String email;
  private Boolean parDefaut = false;
}
