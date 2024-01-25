package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Campus;

@Setter
@Getter
@NoArgsConstructor
public class LiteCampusForSalleDTO {
  private Long id;
  private String code;
  private String libelle;

  public LiteCampusForSalleDTO(Campus campus) {
    this.id = campus.getId();
    this.code = campus.getCode();
    this.libelle = campus.getLibelle();
  }
}
