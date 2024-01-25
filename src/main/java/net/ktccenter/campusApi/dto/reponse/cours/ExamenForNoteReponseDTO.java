package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.cours.Examen;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForNoteReponseDTO extends AbstractDTO {
  private String code;
  private Date dateExamen;
  private Double pourcentage;
  private BigDecimal totalFraisPension = BigDecimal.valueOf(0.0);
  private BigDecimal totalFraisRattrapage = BigDecimal.valueOf(0.0);
  private LiteInscriptionForNoteDTO inscription;
  private LiteEpreuveDTO epreuve;
  private Float moyenne;
  private String appreciation;

  public ExamenForNoteReponseDTO(Examen examen) {
    this.setId(examen.getId());
    this.code = examen.getCode();
    this.dateExamen = examen.getDateExamen();
    this.pourcentage = examen.getPourcentage();
  }
}
