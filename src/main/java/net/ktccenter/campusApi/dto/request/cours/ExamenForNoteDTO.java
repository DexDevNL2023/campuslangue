package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForNoteDTO {
  private Long examenId;
  private Date dateExamen;
  private EpreuveForNoteDTO epreuve;
}
