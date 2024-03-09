package net.ktccenter.campusApi.dto.reponse.branch;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForCampusDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaiementCampusDTO {
    private LiteCampusDTO campus;
    private List<LitePaiementForCampusDTO> data = new ArrayList<>();

    public BigDecimal getSoldeTotalCampus() {
        BigDecimal amount = BigDecimal.valueOf(0.0);
        for (LitePaiementForCampusDTO p : data) {
            amount = amount.add(p.getMontant());
        }
        return amount;
    }
}
