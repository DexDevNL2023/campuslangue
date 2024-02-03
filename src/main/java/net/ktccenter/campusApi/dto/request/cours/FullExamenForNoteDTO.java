package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FullExamenForNoteDTO {
  private Date dateExamen;
  private List<ExamenForNoteDTO> examens;

  public FullExamenForNoteDTO(Date dateExamen, List<ExamenForNoteDTO> examens) {
    this.dateExamen = dateExamen;
    this.examens = examens;
  }
}
