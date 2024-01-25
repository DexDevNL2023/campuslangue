package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteDroitDTO {
    private Long id;
    private String libelle;
    private String key;
    private String verbe;
    private Boolean isDefault;
    private String description;
}
