package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PermissionDTO {
    private Long permissionId;
    private Boolean hasPermission;
}

