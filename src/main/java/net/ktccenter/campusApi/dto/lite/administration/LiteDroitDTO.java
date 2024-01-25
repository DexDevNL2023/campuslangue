package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Droit;

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

    public LiteDroitDTO(Droit droit) {
        this.id = droit.getId();
        this.libelle = droit.getLibelle();
        this.key = droit.getKey();
        this.verbe = droit.getVerbe();
        this.isDefault = droit.getIsDefault();
        this.description = droit.getDescription();
    }
}
