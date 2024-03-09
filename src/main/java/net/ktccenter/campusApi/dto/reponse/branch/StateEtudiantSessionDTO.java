package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StateEtudiantSessionDTO {
    private LiteSessionForStateDTO session;
    private List<LiteEtudiantForStateDTO> data = new ArrayList<>();

    public int getNbreTotelEtudiant() {
        return data.size();
    }
}
