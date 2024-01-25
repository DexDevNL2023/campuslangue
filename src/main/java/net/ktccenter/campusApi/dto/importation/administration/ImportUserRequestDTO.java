package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ImportUserRequestDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    private String prenom;
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    private String langKey;
    @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
    private String imageUrl;
    private Boolean isVerified;
    private Date created;
    private Date updated;
    private Boolean isDefault;
    private Boolean enabled;
    private Set<String> roleLibelles;
    @EnumValidator(enumClass = TypeUser.class)
    private TypeUser typeUser;
}
