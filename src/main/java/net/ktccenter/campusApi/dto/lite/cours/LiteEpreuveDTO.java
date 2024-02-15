package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.Epreuve;

@Setter
@Getter
@NoArgsConstructor
public class LiteEpreuveDTO {
  private Long id;
  private LiteUniteDTO unite;
  private Float noteObtenue = 0.0F;
  private Boolean estValidee;
  private Float noteRattrapage = 0.0F;
  private Boolean estRattrapee;

  public LiteEpreuveDTO(Epreuve epreuve) {
    this.id = epreuve.getId();
    this.noteObtenue = epreuve.getNoteObtenue();
    this.estValidee = epreuve.getEstValidee();
    this.noteRattrapage = epreuve.getNoteRattrapage();
    this.estRattrapee = epreuve.getEstRattrapee();
  }
}
