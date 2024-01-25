package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class DroitDTO extends AbstractDTO {
    private Long id;
    private String libelle;
    private String key;
    private String verbe;
    private Boolean isDefault;
    private String description;
    private LiteModuleDTO module;
}
