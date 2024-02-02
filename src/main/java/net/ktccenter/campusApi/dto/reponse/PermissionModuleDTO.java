package net.ktccenter.campusApi.dto.reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PermissionModuleDTO {
    private LiteModuleDTO module;
    private List<LiteRoleDroitDTO> data = new ArrayList<>();
}
