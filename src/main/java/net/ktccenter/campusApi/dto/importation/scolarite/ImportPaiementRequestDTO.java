package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportPaiementRequestDTO {
  @NotBlank(message = "La reference du paiement est obligatoire")
  @Size(min = 2, message = "La reference doit être d'au moins 2 caractères")
  private String refPaiement;
  @DecimalMin(value = "0", inclusive = false)
  private BigDecimal montant;
  @NotNull(message = "la date du paiement est obligatoire")
  private Instant date;
  private String modePaiementCode;
  private String rubriqueCode;
  private String compteCode;
}
