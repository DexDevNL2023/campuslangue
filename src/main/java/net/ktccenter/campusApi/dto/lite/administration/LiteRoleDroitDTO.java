package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.RoleDroit;

@Setter
@Getter
@NoArgsConstructor
public class LiteRoleDroitDTO {
    private Long id;
    private LiteDroitDTO droit;
    private Boolean hasDroit;

    public LiteRoleDroitDTO(RoleDroit permission) {
        this.id = permission.getId();
        this.hasDroit = permission.getHasDroit();
    }
}
