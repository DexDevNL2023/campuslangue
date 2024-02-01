package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.enums.TypeUser;

@Setter
@Getter
@NoArgsConstructor
public class LiteUserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String imageUrl;
    private LiteRoleDTO role;
    private TypeUser typeUser;
    private LiteBrancheDTO branche;
    private Boolean isGrant;
    private String fullName;

    public LiteUserDTO(User user) {
        this.id = user.getId();
        this.nom = user.getNom();
        this.prenom = user.getPrenom();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.typeUser = user.getTypeUser();
        this.isGrant = user.getIsGrant();
    }

    public String getFullName() {
        return this.nom + " " + this.prenom;
    }
}
