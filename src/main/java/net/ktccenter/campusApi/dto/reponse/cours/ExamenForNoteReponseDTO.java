package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.cours.Examen;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForNoteReponseDTO extends AbstractDTO {
  private String code;
  private Date dateExamen;
  private Double pourcentage;
  private String matricule;
  private String nom;
  private String prenom;
  private String fullName;
  private Set<LiteEpreuveDTO> epreuves = new HashSet<>();
  private Float moyenne = 0.0F;
  private String appreciation;

  public ExamenForNoteReponseDTO(Examen examen) {
    this.setId(examen.getId());
    this.code = examen.getCode();
    this.dateExamen = examen.getDateExamen();
    this.pourcentage = examen.getPourcentage();
  }
}
