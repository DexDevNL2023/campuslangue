package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModulePermissionDTO {

    private Long id;
    private String name;
    private List<PermissionResponseDTO> permissions;
}

