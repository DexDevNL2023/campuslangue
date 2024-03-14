package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Module;

@Setter
@Getter
@NoArgsConstructor
public class LiteModuleDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean hasDroit;

    public LiteModuleDTO(Module module) {
        this.id = module.getId();
        this.name = module.getName();
        this.description = module.getDescription();
        this.hasDroit = module.getHasDroit();
    }
}
