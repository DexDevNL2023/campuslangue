package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Niveau;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class LiteNiveauDTO {
    private Long id;
    private String code;
    private String libelle;
    private BigDecimal fraisInscription = new BigDecimal("0.0");
    private BigDecimal fraisPension = new BigDecimal("0.0");
    private BigDecimal fraisRattrapage = new BigDecimal("0.0");
    private Float dureeSeance;
    private LiteDiplomeDTO diplomeRequis;
    private LiteDiplomeDTO diplomeFinFormation;

    public LiteNiveauDTO(Niveau niveau) {
        this.id = niveau.getId();
        this.code = niveau.getCode();
        this.libelle = niveau.getLibelle();
        this.fraisInscription = niveau.getFraisInscription();
        this.fraisPension = niveau.getFraisPension();
        this.fraisRattrapage = niveau.getFraisRattrapage();
    }
}
