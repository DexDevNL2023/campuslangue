package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;

@Setter
@Getter
@NoArgsConstructor
public class EvaluationTestForNoteDTO {
  private Long evaluationTestId;
    private Float note = 0.0F;
  private String moduleFormationCode;

  public EvaluationTestForNoteDTO(EvaluationTest evaluation) {
    this.evaluationTestId = evaluation.getId();
    this.note = evaluation.getNote();
  }
}