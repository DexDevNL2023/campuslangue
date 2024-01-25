package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ModuleDTO extends AbstractDTO {
    private String name;
    private String description;
    private List<LiteDroitDTO> droits;
}
