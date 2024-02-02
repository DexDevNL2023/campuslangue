package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FullTestModuleForNoteDTO {
  private Date dateEvaluation;
  private List<TestModuleForNoteDTO> testModules;

  public FullTestModuleForNoteDTO(Date dateEvaluation, List<TestModuleForNoteDTO> testModules) {
    this.dateEvaluation = dateEvaluation;
    this.testModules = testModules;
  }
}
