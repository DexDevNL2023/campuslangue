package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaiementBranchDTO {
    private LiteBrancheDTO branche;
    private List<LitePaiementDTO> data = new ArrayList<>();
    private BigDecimal soldeTotal = BigDecimal.valueOf(0.0);

    private BigDecimal getSoldeTotal() {
        BigDecimal soldeTotal = BigDecimal.valueOf(0.0);
        data.stream().map(e -> soldeTotal.add(e.getMontant()));
        return soldeTotal;
    }
}
