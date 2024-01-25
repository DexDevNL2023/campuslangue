package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class AuthRequestDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "le format de l'email est incorrecte", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
