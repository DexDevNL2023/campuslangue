package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UniteDTO extends AbstractDTO {
  private String code;
  private String libelle;
  private Float noteAdmission;
  private LiteNiveauDTO niveau;
  private Set<LiteEpreuveDTO> epreuves = new HashSet<>();
}
