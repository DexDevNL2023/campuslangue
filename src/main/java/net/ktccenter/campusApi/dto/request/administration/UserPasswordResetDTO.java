package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserPasswordResetDTO {
    private String passwordResetCode;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
    @NotBlank(message = "Le mot de passe de confirmation est obligatoire")
    private String confirmPassword;
}
