package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteRoleDroitDTO {
    private Long id;
    private LiteDroitDTO droit;
    private Boolean hasDroit;
}
