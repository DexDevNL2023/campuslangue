package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LitePaiementDTO {
  private Long id;
  private String refPaiement;
  private BigDecimal montant;
  private Instant datePaiement;
  private LiteModePaiementDTO modePaiement;
  private LiteRubriqueDTO rubrique;
  private LiteCompteDTO compte;
  private LiteCampusDTO campus;
}
