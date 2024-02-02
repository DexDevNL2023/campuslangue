package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.TestModule;

@Setter
@Getter
@NoArgsConstructor
public class TestModuleForNoteDTO {
  private Long testModuleId;
  private String matricule;
  private String nom;
  private String prenom;
  private String fullName;
  private EvaluationTestForNoteDTO evaluation;

  public TestModuleForNoteDTO(TestModule testModule) {
    this.testModuleId = testModule.getId();
  }

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
