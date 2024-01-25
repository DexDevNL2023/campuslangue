package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.enums.Jour;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
public class LitePlageHoraireDTO {
  private Long id;
  private String code;
  private LocalTime startTime;
  private LocalTime endTime;
  private Jour jour;

  public LitePlageHoraireDTO(PlageHoraire plage) {
    this.id = plage.getId();
    this.code = plage.getCode();
    this.startTime = plage.getStartTime();
    this.endTime = plage.getEndTime();
    this.jour = plage.getJour();
  }
}
