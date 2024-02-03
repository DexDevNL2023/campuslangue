package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FullTestModuleForNoteImportDTO {
  private Date dateEvaluation;
  private List<EvaluationTestForNoteImportDTO> evaluations;

  public FullTestModuleForNoteImportDTO(Date dateEvaluation, List<EvaluationTestForNoteImportDTO> evaluations) {
    this.dateEvaluation = dateEvaluation;
    this.evaluations = evaluations;
  }
}
