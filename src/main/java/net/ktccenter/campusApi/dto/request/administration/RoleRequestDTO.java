package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class RoleRequestDTO {
    private Long id;
    @NotBlank(message = "le libelle du role est obligatoire")
    private String libelle;
    private Boolean isSuper;
}
