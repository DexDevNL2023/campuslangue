package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.entities.administration.Role;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class RoleDTO extends AbstractDTO {
    private String libelle;
    private Boolean isSuper;
    private List<LiteRoleDroitDTO> permissions;

    public RoleDTO(Role role) {
        this.setId(role.getId());
        this.libelle = role.getLibelle();
        this.isSuper = role.getIsSuper();
    }
}
