package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RubriqueDTO extends AbstractDTO {
    private String code;
    private String libelle;
    private BigDecimal frais;
    private Double reduction;
    private Set<LitePaiementDTO> paiements = new HashSet<>();
}
