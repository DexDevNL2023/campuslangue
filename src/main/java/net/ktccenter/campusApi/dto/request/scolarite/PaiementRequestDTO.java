package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PaiementRequestDTO {
  private Long id;
  @NotBlank(message = "La reference du paiement est obligatoire")
  @Size(min = 2, message = "La reference doit être d'au moins 2 caractères")
  private String refPaiement;
  @DecimalMin(value = "0", inclusive = false)
  private BigDecimal montant;
  @NotNull(message = "la date du paiement est obligatoire")
  private Instant datePaiement;
  private Long modePaiementId;
  private Long rubriqueId;
  private Long compteId;
  private Long campusId;
}
