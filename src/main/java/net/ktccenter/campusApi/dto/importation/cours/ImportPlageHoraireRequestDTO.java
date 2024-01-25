package net.ktccenter.campusApi.dto.importation.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.Jour;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportPlageHoraireRequestDTO {
  @NotBlank(message = "Le code de la plage horaire est obligatoire")
  @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  private Integer startHour;
  private Integer startMinute;
  private Integer endHour;
  private Integer endMinute;
  private Jour jour;
}
