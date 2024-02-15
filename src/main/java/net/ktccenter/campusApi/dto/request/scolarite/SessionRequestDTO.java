package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.NotNull;
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
  @NotNull(message = "Le code de la session est obligatoire")
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
