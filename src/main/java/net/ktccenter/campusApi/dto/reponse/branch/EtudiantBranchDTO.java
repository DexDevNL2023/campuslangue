package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EtudiantBranchDTO {
    private LiteBrancheDTO branche;
    private List<LiteEtudiantDTO> data = new ArrayList<>();
}
