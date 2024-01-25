package net.ktccenter.campusApi.dto.request.cours;

import lombok.*;
import net.ktccenter.campusApi.enums.Jour;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PlageHoraireRequestDTO {
  private Long id;
  @NotBlank(message = "Le code de la plage horaire est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  private Integer startHour;
  private Integer startMinute;
  private Integer endHour;
  private Integer endMinute;
  private Jour jour;
}
