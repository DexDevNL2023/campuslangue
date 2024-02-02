package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.entities.scolarite.Session;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteSessionForCompteDTO {
  private Long id;
  private String code;
  private Boolean estTerminee;
  private LiteNiveauForSessionDTO niveau;

  public LiteSessionForCompteDTO(Session session) {
    this.id = session.getId();
    this.code = session.getCode();
    this.estTerminee = session.getEstTerminee();
  }
}
