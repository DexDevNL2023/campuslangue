package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class EvaluationTestDTO extends AbstractDTO {
  private Date dateEvaluation;
    private Float note = 0.0F;
  private LiteTestModuleDTO testModule;
  private LiteModuleFormationDTO moduleFormation;
}