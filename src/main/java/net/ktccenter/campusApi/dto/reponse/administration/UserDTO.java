package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.enums.TypeUser;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String langKey;
    private String imageUrl;
    private Set<LiteRoleDTO> roles;
    private TypeUser typeUser;
    private LiteBrancheDTO branche;
    private String fullName;

    public String getFullName() {
        return this.nom + " " + this.prenom;
    }
}
