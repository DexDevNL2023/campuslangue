package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class EpreuveDTO extends AbstractDTO {
  private LiteUniteDTO unite;
    private Float noteObtenue = 0.0F;
  private Boolean estValidee;
    private Float noteRattrapage = 0.0F;
  private Boolean estRattrapee;
  private LiteExamenDTO examen;
}
