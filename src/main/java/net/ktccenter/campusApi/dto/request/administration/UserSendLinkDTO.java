package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserSendLinkDTO {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "le format de l'email est incorrecte", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
}
