package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LitePermissionResponseDTO {
    private Long id;
    private String permission;
    private Boolean hasPermission;
}

