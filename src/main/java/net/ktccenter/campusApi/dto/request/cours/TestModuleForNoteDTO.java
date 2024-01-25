package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TestModuleForNoteDTO {
  private Long testModuleId;
  private EvaluationTestForNoteDTO evaluation;
}
