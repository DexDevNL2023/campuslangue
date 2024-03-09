package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaiementBranchAndCampusDTO {
    private LiteBrancheDTO branche;
    private List<PaiementCampusDTO> data = new ArrayList<>();

    public BigDecimal getSoldeTotalBranch() {
        BigDecimal amount = BigDecimal.valueOf(0.0);
        for (PaiementCampusDTO p : data) {
            amount = amount.add(p.getSoldeTotalCampus());
        }
        return amount;
    }
}
