package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class SessionRequestDTO {
  private Long id;
    @NotBlank(message = "Le code de la vague est obligatoire")
    @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
  private String code;
  @NotNull(message = "La date de debut de la session est obligatoire")
  private Date dateDebut;
  @NotNull(message = "La date de fin de la session est obligatoire")
  private Date dateFin;
  private Boolean estTerminee = false;
  private Long brancheId;
  private Long niveauId;
  private Long vagueId;
  private Set<Long> occupationIds = new HashSet<>();
  private Long formateurId;
}
