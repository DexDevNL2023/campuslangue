package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InscriptionBranchDTO {
    private LiteBrancheDTO branche;
    private List<LiteInscriptionDTO> data = new ArrayList<>();
}
