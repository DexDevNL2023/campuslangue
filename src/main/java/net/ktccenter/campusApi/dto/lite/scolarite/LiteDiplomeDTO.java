package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class LiteDiplomeDTO extends AbstractDTO {
    private String code;
    private String libelle;

    public LiteDiplomeDTO(Long id, String code, String libelle) {
        this.setId(id);
        this.code = code;
        this.libelle = libelle;
    }
}
