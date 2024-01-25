package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ModuleFormationDTO extends AbstractDTO {
  private String code;
  private String libelle;
  private LiteNiveauDTO niveau;
  private Set<LiteEvaluationTestDTO> tests = new HashSet<>();
}
