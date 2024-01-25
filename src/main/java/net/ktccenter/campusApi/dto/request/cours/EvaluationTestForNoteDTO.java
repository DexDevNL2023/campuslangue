package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class EvaluationTestForNoteDTO {
  private Long evaluationTestId;
  private Date dateEvaluation;
  private Float note;
}