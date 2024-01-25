package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class InscrireExitStudientRequestDTO {
  // Données paiement frais inscription
  @NotBlank(message = "La reference du paiement est obligatoire")
  @Size(min = 2, message = "La reference doit être d'au moins 2 caractères")
  private String refPaiement;
  @NotNull(message = "la date du paiement est obligatoire")
  private Instant datePaiement;
  private Long modePaiementId;
  private Long rubriqueId;
  // Données inscription
  @NotNull(message = "la date de l'inscription est obligatoire")
  private Date dateInscription;
  private Boolean estNouveau;
  private Boolean estRedoublant;
  private BigDecimal fraisInscription;
  private BigDecimal avancePension;
  private Long sessionId;
  private Long campusId;
  // Données etudiant
  private Long etudiantId;
}
