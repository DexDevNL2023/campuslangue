package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.enums.TypeUser;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String langKey;
    private String imageUrl;
    private Boolean isVerified;
    private String verificationCode;
    private String passwordResetCode;
    private Date created;
    private Date updated;
    private Boolean isDefault;
    private Boolean enabled;
    private Set<LiteRoleDTO> roles;
    private TypeUser typeUser;
}
