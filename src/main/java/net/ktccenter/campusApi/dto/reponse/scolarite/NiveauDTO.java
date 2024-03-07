package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class NiveauDTO extends AbstractDTO {
    private String code;
    private String libelle;
    private BigDecimal fraisInscription;
    private BigDecimal fraisPension;
    private BigDecimal fraisRattrapage;
    private LiteDiplomeDTO diplomeRequis;
    private LiteDiplomeDTO diplomeFinFormation;
    private Float dureeSeance;
    Set<LiteSessionDTO> sessions = new HashSet<>();
    Set<LiteModuleFormationDTO> modules = new HashSet<>();
    Set<LiteUniteDTO> unites = new HashSet<>();
}
