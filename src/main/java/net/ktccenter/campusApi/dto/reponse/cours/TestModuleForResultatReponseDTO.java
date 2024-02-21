package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestForNoteDTO;
import net.ktccenter.campusApi.entities.cours.TestModule;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class TestModuleForResultatReponseDTO {
  private String matricule;
  private String fullName;
  private Set<EvaluationTestForNoteDTO> evaluations = new HashSet<>();
  private Float moyenne = 0.0F;
}
