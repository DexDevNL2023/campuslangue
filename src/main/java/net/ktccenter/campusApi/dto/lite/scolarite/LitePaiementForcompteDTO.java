package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Paiement;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LitePaiementForcompteDTO {
  private Long id;
  private String refPaiement;
  private BigDecimal montant;
  private Instant datePaiement;

  public LitePaiementForcompteDTO(Paiement paiement) {
    this.id = paiement.getId();
    this.refPaiement = paiement.getRefPaiement();
    this.montant = paiement.getMontant();
    this.datePaiement = paiement.getDatePaiement();
  }
}
