package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class TestModuleDTO extends AbstractDTO {
  private String code;
  private Double pourcentage;
  private LiteInscriptionForNoteDTO inscription;
  private Set<LiteEvaluationTestDTO> evaluations = new HashSet<>();
  private Float moyenne;
}
