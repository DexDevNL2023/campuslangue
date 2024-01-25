package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.enums.Sexe;
import net.ktccenter.campusApi.validators.EnumValidator;

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
public class InscrireNewStudientRequestDTO {
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
  @NotBlank(message = "Le nom de l'apprenant est obligatoire")
  private String nom;
  private String prenom;
  @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
  private String imageUrl;
  @NotBlank(message = "Nationalité est obligatoire")
  private String nationalite;
  @EnumValidator(enumClass = Sexe.class)
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  @NotBlank(message = "Tuteur est obligatoire")
  private String tuteur;
  @NotBlank(message = "Contact du tuteur est obligatoire")
  private String contactTuteur;
  private Long dernierDiplomeId;
}
