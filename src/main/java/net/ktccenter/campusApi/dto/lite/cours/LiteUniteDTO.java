package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.entities.cours.Unite;

@Setter
@Getter
@NoArgsConstructor
public class LiteUniteDTO {
  private Long id;
  private String code;
  private String libelle;
  private Float noteAdmission;
  private LiteNiveauDTO niveau;

  public LiteUniteDTO(Unite unite) {
    this.id = unite.getId();
    this.code = unite.getCode();
    this.libelle = unite.getLibelle();
    this.noteAdmission = unite.getNoteAdmission();
  }
}
