package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class UpdatePasswordDTO {
    @NotBlank(message = "Le ancien mot de passe est obligatoire")
    private String OldPassword;
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    private String newPassword;
    @NotBlank(message = "Le mot de passe de confirmation est obligatoire")
    private String matchingPassword;
}
