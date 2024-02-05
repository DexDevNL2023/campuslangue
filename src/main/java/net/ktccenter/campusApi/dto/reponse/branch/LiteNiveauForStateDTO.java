package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Niveau;

@Setter
@Getter
@NoArgsConstructor
public class LiteNiveauForStateDTO {
    private Long id;
    private String code;
    private String libelle;

    public LiteNiveauForStateDTO(Niveau niveau) {
        this.id = niveau.getId();
        this.code = niveau.getCode();
        this.libelle = niveau.getLibelle();
    }
}
