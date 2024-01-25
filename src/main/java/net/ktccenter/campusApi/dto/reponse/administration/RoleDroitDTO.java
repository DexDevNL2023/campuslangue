package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class RoleDroitDTO extends AbstractDTO {
    private LiteRoleDTO role;
    private LiteDroitDTO droit;
    private Boolean hasDroit;
}
