package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserBranchDTO {
    private LiteBrancheDTO branche;
    private List<LiteUserDTO> data = new ArrayList<>();
}
