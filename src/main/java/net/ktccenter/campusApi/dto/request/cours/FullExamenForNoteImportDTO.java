package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FullExamenForNoteImportDTO {
  private Date dateExamen;
  private List<EpreuveForNoteImportDTO> epreuves;

  public FullExamenForNoteImportDTO(Date dateExamen, List<EpreuveForNoteImportDTO> epreuves) {
    this.dateExamen = dateExamen;
    this.epreuves = epreuves;
  }
}
