package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.Role;

@Setter
@Getter
@NoArgsConstructor
public class LiteRoleDTO {
    private Long id;
    private String libelle;
    private Boolean isSuper;

    public LiteRoleDTO(Role role) {
        this.id = role.getId();
        this.libelle = role.getLibelle();
        this.isSuper = role.getIsSuper();
    }
}
