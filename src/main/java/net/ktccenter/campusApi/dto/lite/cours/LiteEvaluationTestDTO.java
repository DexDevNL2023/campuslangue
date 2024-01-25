package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class LiteEvaluationTestDTO {
  private Long id;
  private Date dateEvaluation;
  private Float note;
  private LiteModuleFormationDTO moduleFormation;

  public LiteEvaluationTestDTO(EvaluationTest evaluation) {
    this.id = evaluation.getId();
    this.dateEvaluation = evaluation.getDateEvaluation();
    this.note = evaluation.getNote();
  }
}