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

    public Long getNbreTotelEtudiant() {
        long count = 0L;
        for (StateEtudiantSessionDTO d : data) {
            count = count + d.getNbreTotelEtudiant();
        }
        return count;
    }
}
