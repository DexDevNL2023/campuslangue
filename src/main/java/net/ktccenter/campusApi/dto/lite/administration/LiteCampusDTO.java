package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Campus;

@Setter
@Getter
@NoArgsConstructor
public class LiteCampusDTO {
  private Long id;
  private String code;
  private String libelle;
  private String adresse;
  private LiteBrancheForCampusDTO branche;

  public LiteCampusDTO(Campus campus) {
    this.id = campus.getId();
    this.code = campus.getCode();
    this.libelle = campus.getLibelle();
    this.adresse = campus.getAdresse();
  }
}
