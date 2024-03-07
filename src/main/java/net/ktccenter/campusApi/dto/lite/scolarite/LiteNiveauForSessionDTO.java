package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Niveau;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class LiteNiveauForSessionDTO {
    private Long id;
    private String code;
    private String libelle;
    private BigDecimal fraisInscription;
    private BigDecimal fraisPension;
    private BigDecimal fraisRattrapage;
    private Float dureeSeance;

    public LiteNiveauForSessionDTO(Niveau niveau) {
        this.id = niveau.getId();
        this.code = niveau.getCode();
        this.libelle = niveau.getLibelle();
        this.fraisInscription = niveau.getFraisInscription();
        this.fraisPension = niveau.getFraisPension();
        this.fraisRattrapage = niveau.getFraisRattrapage();
    }
}
