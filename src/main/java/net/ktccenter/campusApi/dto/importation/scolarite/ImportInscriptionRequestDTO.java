package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportInscriptionRequestDTO {
  @NotNull(message = "la date de l'inscription est obligatoire")
  private Date dateInscription;
  private Date dateDelivranceAttestation;
  private Boolean estRedoublant;
  private String sessionCode;
  private String etudiantMatricule;
  private String campusCode;
}
