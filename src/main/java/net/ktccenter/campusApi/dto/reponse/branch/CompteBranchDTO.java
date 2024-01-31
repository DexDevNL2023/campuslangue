package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CompteBranchDTO {
    private LiteBrancheDTO branche;
    private List<LiteCompteDTO> data = new ArrayList<>();
    private BigDecimal soldeTotal = BigDecimal.valueOf(0.0);

    private BigDecimal getSoldeTotal() {
        BigDecimal soldeTotal = BigDecimal.valueOf(0.0);
        data.stream().map(e -> soldeTotal.add(e.getSolde()));
        return soldeTotal;
    }
}
