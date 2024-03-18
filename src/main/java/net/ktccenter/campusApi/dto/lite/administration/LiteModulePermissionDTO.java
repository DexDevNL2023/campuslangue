package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Module;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LiteModulePermissionDTO {
    private Long id;
    private String name;
    private Boolean hasDroit;

    public LiteModulePermissionDTO(Module module) {
        this.id = module.getId();
        this.name = module.getName();
        this.hasDroit = module.getHasDroit();
    }
}

