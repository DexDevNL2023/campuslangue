package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponseDTO {
    private Long id;
    private String permission;
    private Boolean hasPermission;
}

