package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class InscriptionRequestDTO {
  private Long id;
  @NotNull(message = "la date de l'inscription est obligatoire")
  private Date dateInscription;
  private Date dateDelivranceAttestation;
  private Boolean estRedoublant;
  private Long sessionId;
  private Long etudiantId;
  private Long campusId;
}
