package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;
import net.ktccenter.campusApi.enums.TypeUser;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserRequestDTO {
    private Long id;
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    private String prenom;
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
    private String imageUrl;
    private Long roleId;
    @EnumValidator(enumClass = TypeUser.class)
    private TypeUser typeUser;
    private Long brancheId;
}
