package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveForNoteDTO;
import net.ktccenter.campusApi.entities.cours.Examen;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForResultatReponseDTO {
  private String matricule;
  private String fullName;
  private Set<EpreuveForNoteDTO> epreuves = new HashSet<>();
  private Float moyenne = 0.0F;
  private String appreciation;
}
