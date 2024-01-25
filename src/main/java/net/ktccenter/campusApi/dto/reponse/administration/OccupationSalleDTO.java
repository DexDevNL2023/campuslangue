package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OccupationSalleDTO extends AbstractDTO {
  private String code;
  private Boolean estOccupee;
  private LitePlageHoraireDTO plageHoraire;
  private LiteSalleDTO salle;

  public OccupationSalleDTO(OccupationSalle occupation) {
    super();
    this.setId(occupation.getId());
    this.code = occupation.getCode();
    this.estOccupee = occupation.getEstOccupee();
  }
}
