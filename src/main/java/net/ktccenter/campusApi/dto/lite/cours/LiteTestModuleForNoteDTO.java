package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.TestModule;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class LiteTestModuleForNoteDTO {
  private Long id;
  private Set<LiteEvaluationTestForNoteDTO> evaluations = new HashSet<>();
  private Float moyenne;

  public LiteTestModuleForNoteDTO(TestModule testModule) {
    this.id = testModule.getId();
  }
}
