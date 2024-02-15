package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FullExamen2ForNoteDTO {
  private Date dateExamen;
  private List<Examen2ForNoteDTO> examens;

  public FullExamen2ForNoteDTO(Date dateExamen, List<Examen2ForNoteDTO> examens) {
    this.dateExamen = dateExamen;
    this.examens = examens;
  }
}
