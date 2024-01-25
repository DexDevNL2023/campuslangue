package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.entities.cours.TestModule;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class LiteTestModuleDTO {
  private Long id;
  private String code;
  private Double pourcentage;
  private LiteInscriptionDTO inscription;
  private Set<LiteEvaluationTestDTO> evaluations = new HashSet<>();
  private Float moyenne;

  public LiteTestModuleDTO(TestModule testModule) {
    this.id = testModule.getId();
    this.code = testModule.getCode();
    this.pourcentage = testModule.getPourcentage();
  }
}
