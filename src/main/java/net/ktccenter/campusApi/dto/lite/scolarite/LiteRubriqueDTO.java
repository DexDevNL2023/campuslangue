package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class LiteRubriqueDTO {
    private Long id;
	private String code;
    private String libelle;
    private BigDecimal frais;
    private Double reduction;

    public LiteRubriqueDTO(Rubrique rubrique) {
        this.id = rubrique.getId();
        this.code = rubrique.getCode();
        this.libelle = rubrique.getLibelle();
        this.frais = rubrique.getFrais();
        this.reduction = rubrique.getReduction();
    }
}
