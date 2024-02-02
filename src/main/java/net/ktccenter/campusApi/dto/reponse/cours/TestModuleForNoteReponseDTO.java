package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.cours.TestModule;

@Setter
@Getter
@NoArgsConstructor
public class TestModuleForNoteReponseDTO extends AbstractDTO {
  private String code;
  private Double pourcentage;
  private String matricule;
  private String nom;
  private String prenom;
  private String fullName;
  private LiteEvaluationTestDTO evaluation;
    private Float moyenne = 0.0F;

  public TestModuleForNoteReponseDTO(TestModule testModule) {
    this.setId(testModule.getId());
    this.code = testModule.getCode();
    this.pourcentage = testModule.getPourcentage();
  }
}
