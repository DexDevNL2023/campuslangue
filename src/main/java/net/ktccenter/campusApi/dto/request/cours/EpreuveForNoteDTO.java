package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.Epreuve;

@Setter
@Getter
@NoArgsConstructor
public class EpreuveForNoteDTO {
  private Long epreuveId;
  private Float noteObtenue = 0.0F;
  private Float noteRattrapage = 0.0F;
  private String uniteCode;

  public EpreuveForNoteDTO(Epreuve epreuve) {
    this.epreuveId = epreuve.getId();
    this.noteObtenue = epreuve.getNoteObtenue();
    this.noteRattrapage = epreuve.getNoteRattrapage();
  }
}
