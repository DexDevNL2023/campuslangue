package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.entities.scolarite.Session;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteSessionDTO {
  private Long id;
  private String code;
  private Date dateDebut;
  private Date dateFin;
  private Boolean estTerminee;
  private LiteBrancheDTO branche;

  public LiteSessionDTO(Session session) {
    this.id = session.getId();
    this.code = session.getCode();
    this.dateDebut = session.getDateDebut();
    this.dateFin = session.getDateFin();
    this.estTerminee = session.getEstTerminee();
  }
}
