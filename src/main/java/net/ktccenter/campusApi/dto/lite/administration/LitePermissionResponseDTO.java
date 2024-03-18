package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.RoleDroit;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LitePermissionResponseDTO {
    private Long id;
    private String permission;
    private Boolean hasPermission;

    public LitePermissionResponseDTO(RoleDroit permission) {
        this.id = permission.getId();
        this.permission = permission.getDroit().getKey();
        this.hasPermission = permission.getHasDroit();
    }
}

