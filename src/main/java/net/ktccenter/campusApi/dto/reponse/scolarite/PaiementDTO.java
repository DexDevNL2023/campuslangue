package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaiementDTO extends AbstractDTO {
  private String refPaiement;
  private BigDecimal montant;
  private Instant datePaiement;
  private LiteModePaiementDTO modePaiement;
  private LiteRubriqueDTO rubrique;
  private LiteCompteDTO compte;
  private LiteCampusDTO campus;
}
