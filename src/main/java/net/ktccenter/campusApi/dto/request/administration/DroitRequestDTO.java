package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class DroitRequestDTO {
    private Long id;
    @NotBlank(message = "le libelle du droit est obligatoire")
    private String libelle;
    @NotBlank(message = "La clé du droit est obligatoire")
    @Size(min = 2, message = "La clé doit être d'au moins 2 caractères")
    private String key;
    @NotBlank(message = "La verbe du droit est obligatoire")
    private String verbe;
    private String description;
    private Long moduleId;
}
