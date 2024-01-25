package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class VagueRequestDTO {
  private Long id;
  @NotBlank(message = "Le code de la vague est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
}
