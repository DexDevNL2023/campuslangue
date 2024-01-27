package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class CampusDTO extends AbstractDTO {
  private String code;
  private String libelle;
  private String adresse;
  private LiteBrancheDTO branche;
  private int nombreSalle;
}
