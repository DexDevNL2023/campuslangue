package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.entities.scolarite.Session;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteSessionForStateDTO {
    private Long id;
    private String code;
    private Boolean estTerminee;
    private LiteNiveauForStateDTO niveau;

    public LiteSessionForStateDTO(Session session) {
        this.id = session.getId();
        this.code = session.getCode();
        this.estTerminee = session.getEstTerminee();
    }
}
