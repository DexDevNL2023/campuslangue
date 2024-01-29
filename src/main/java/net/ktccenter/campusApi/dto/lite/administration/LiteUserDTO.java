package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.TypeUser;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class LiteUserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String imageUrl;
    private Set<LiteRoleDTO> roles;
    private TypeUser typeUser;
    private Boolean isGrant;
    private String fullName;

    public String getFullName() {
        return this.nom + " " + this.prenom;
    }
}
