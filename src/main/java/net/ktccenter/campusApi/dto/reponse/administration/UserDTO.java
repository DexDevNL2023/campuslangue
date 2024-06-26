package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.enums.TypeUser;

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
    private LiteRoleDTO role;
    private TypeUser typeUser;
    private LiteBrancheDTO branche;
    private Boolean isGrant;
    private String fullName;

    public String getFullName() {
        return this.nom + " " + this.prenom;
    }
}
