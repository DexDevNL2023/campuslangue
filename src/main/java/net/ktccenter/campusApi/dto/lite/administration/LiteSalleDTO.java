package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Salle;

@Setter
@Getter
@NoArgsConstructor
public class LiteSalleDTO {
  private Long id;
  private String code;
  private String libelle;
  private Integer capacite;
  private LiteCampusForSalleDTO campus;

  public LiteSalleDTO(Salle salle) {
    this.id = salle.getId();
    this.code = salle.getCode();
    this.libelle = salle.getLibelle();
    this.capacite = salle.getCapacite();
  }
}
