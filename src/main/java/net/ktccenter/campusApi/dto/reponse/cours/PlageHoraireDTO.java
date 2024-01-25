package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.enums.Jour;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PlageHoraireDTO extends AbstractDTO {
  private String code;
  private LocalTime startTime;
  private LocalTime endTime;
  private Jour jour;
  private Set<LiteOccupationSalleDTO> occupations = new HashSet<>();
}
