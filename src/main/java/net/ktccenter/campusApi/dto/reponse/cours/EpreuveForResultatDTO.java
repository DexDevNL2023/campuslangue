package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.Epreuve;

@Setter
@Getter
@NoArgsConstructor
public class EpreuveForResultatDTO {
  private Long epreuveId;
  private Float noteFinal = 0.0F;
  private Float noteExamen = 0.0F;
  private Float noteRattrapage = 0.0F;
  private String uniteCode;
  private Boolean estValidee = false;

  public EpreuveForResultatDTO(Epreuve epreuve) {
    this.epreuveId = epreuve.getId();
    this.noteExamen = epreuve.getNoteObtenue();
    this.noteRattrapage = epreuve.getNoteRattrapage();
    this.estValidee = epreuve.getEstValidee();
  }
}
