package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StateEtudiantBranchDTO {
    private LiteBrancheForStateDTO branche;
    private List<StateEtudiantSessionDTO> data = new ArrayList<>();
    private Long nbreTotelEtudiant;

    public Long getNbreTotelEtudiant() {
        return data.stream().count();
    }
}
