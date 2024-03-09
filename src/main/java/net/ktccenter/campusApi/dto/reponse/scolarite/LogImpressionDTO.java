package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.scolarite.LogImpression;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LogImpressionDTO extends AbstractDTO {
  private LiteInscriptionDTO inscription;

  public LogImpressionDTO(LogImpression logImpression) {
    this.setId(logImpression.getId());
    this.setCreatedByUser(logImpression.getCreatedByUser());
    this.setCreatedDate(logImpression.getCreatedDate());
  }
}
