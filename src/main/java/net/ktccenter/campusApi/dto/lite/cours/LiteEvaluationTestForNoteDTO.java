package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class LiteEvaluationTestForNoteDTO {
  private Long id;
  private Date dateEvaluation;
  private Float note;

  public LiteEvaluationTestForNoteDTO(EvaluationTest evaluation) {
    this.id = evaluation.getId();
    this.dateEvaluation = evaluation.getDateEvaluation();
    this.note = evaluation.getNote();
  }
}