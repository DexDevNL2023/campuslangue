package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
    private String imageUrl;
    private String roleLibelle;
    private Boolean isGrant = false;
    @EnumValidator(enumClass = TypeUser.class)
    private TypeUser typeUser;
    private String brancheCode;
}
