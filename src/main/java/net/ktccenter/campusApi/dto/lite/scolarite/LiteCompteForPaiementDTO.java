package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Compte;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class LiteCompteForPaiementDTO {
    private Long id;
    private String code;
    private BigDecimal solde;
    private BigDecimal resteApayer;
    private LiteInscriptionForPaiementDTO inscription;

    public LiteCompteForPaiementDTO(Compte compte) {
        this.id = compte.getId();
        this.code = compte.getCode();
    }
}
