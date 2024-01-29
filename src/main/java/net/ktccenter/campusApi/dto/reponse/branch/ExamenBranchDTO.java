package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ExamenBranchDTO {
    private LiteBrancheDTO branche;
    private List<LiteExamenDTO> data = new ArrayList<>();
}
