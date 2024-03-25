package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ImportSessionRequestDTO {
    @NotBlank(message = "Le code de la vague est obligatoire")
    @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotNull(message = "La date de debut de la session est obligatoire")
  private Date dateDebut;
  @NotNull(message = "La date de fin de la session est obligatoire")
  private Date dateFin;
  private Boolean estTerminee = false;
  private String brancheCode;
  private String niveauCode;
  private String vagueCode;
  public Set<String> occupationCodes = new HashSet<>();
  private String formateurMatricule;
}
