package net.ktccenter.campusApi.dto.reponse.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ExamenDTO extends AbstractDTO {
  private String code;
  private Date dateExamen;
  private Double pourcentage;
  private BigDecimal totalFraisPension;
  private BigDecimal totalFraisRattrapage;
  private LiteInscriptionForNoteDTO inscription;
  private Set<LiteEpreuveDTO> epreuves = new HashSet<>();
  private Float moyenne;
  private String appreciation;
}
