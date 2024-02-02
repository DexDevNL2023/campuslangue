package net.ktccenter.campusApi.dto.lite.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.Examen;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class LiteExamenDTO {
  private Long id;
  private String code;
  private Date dateExamen;
  private Double pourcentage;
  private BigDecimal totalFraisPension;
  private BigDecimal totalFraisRattrapage;
  private Set<LiteEpreuveDTO> epreuves = new HashSet<>();
    private Float moyenne = 0.0F;
  private String appreciation;

  public LiteExamenDTO(Examen examen) {
    this.id = examen.getId();
    this.code = examen.getCode();
    this.dateExamen = examen.getDateExamen();
    this.pourcentage = examen.getPourcentage();
  }
}
