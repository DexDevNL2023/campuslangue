package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForResultatReponseDTO {
  private String matricule;
  private String fullName;
  private Set<EpreuveForResultatDTO> epreuves = new HashSet<>();
  private Float moyenne = 0.0F;
  private String appreciation;
}
