package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LiteOccupationSalleDTO {
  private Long id;
  private String code;
  private Boolean estOccupee;
  private LitePlageHoraireDTO plageHoraire;

  public LiteOccupationSalleDTO(OccupationSalle occupation) {
    this.estOccupee = occupation.getEstOccupee();
    this.code = occupation.getCode();
    this.id = occupation.getId();
  }
}
