package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SessionDTO extends AbstractDTO {
  private String code;
  private Date dateDebut;
  private Date dateFin;
  private Boolean estTerminee;
  private LiteBrancheDTO branche;
  private LiteNiveauDTO niveau;
  private LiteVagueDTO vague;
  private LiteSalleDTO salle;
  public Set<LiteOccupationSalleDTO> occupations = new HashSet<>();
  private LiteFormateurDTO formateur;
  private Set<LiteInscriptionForNoteDTO> inscriptions = new HashSet<>();
}
